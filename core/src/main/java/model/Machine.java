package model;

import commissionStrategies.*;
import decisionStrategies.*;
import exceptions.*;
import lombok.*;
import org.joda.time.*;
import takeProfitStrategies.*;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Getter
public class Machine implements IMoneyStateSupport {

    @Setter
    private Portfolio portfolio;
    @Setter
    private Position position;
    private Position previousPosition;

    private int depth;
    @Setter
    private double currentMoney;
    @Setter
    private boolean isBlocked;
    @Setter
    private DecisionStrategy decisionStrategy;
    @Setter
    private ICommissionStrategy commissionStrategy;
    @Setter
    private ITakeProfitStrategy profitStrategy;

    public static Machine with(Portfolio portfolio, DecisionStrategy decisionStrategy, double commission, int depth) {

        Machine machine = new Machine();

        machine.portfolio = portfolio;
        machine.decisionStrategy = decisionStrategy;
        machine.depth = depth;

        machine.commissionStrategy = CommissionStrategyFactory.createCommissionStrategy(commission);
        machine.profitStrategy = TakeProfitStrategyFactory.createTakeProfitStrategy();

        return machine;
    }

    public Machine() {
        position = Position.begining();
        previousPosition = Position.begining();
        currentMoney = 0;
        isBlocked = false;
    }

    public void apply(Position newPosition) throws PositionAlreadySetFailure {

        if (position.hasSameDirectionAs(newPosition))
            throw new PositionAlreadySetFailure("for machine " + print());

        currentMoney -= computeClosingCommission(newPosition);
        currentMoney -= computeOpeningCommission(newPosition);
        currentMoney += position.computeProfit(newPosition.getValue());

        position = newPosition;
    }

    private String print() {
        return "depth; " + portfolio.printStrategy();
    }

    public void addOrderTo(List<Order> orders) throws Throwable {

        Position newPosition = decisionStrategy.computeNewPositionFor(depth, computeVolume());

        if (position.hasSameDirectionAs(newPosition) || newPosition.isHold())
            return;

        if (isBlocked)
            return;

        if (profitStrategy.shouldTakeProfit(portfolio.getCandlesStorage(), position))
            newPosition = Position.closing(portfolio.getLastCandle());

//        if (!position.isNeutral()) {
//            previousPosition = position.copy();
//            newPosition.neutral();
//        } else if (!decisionStrategy.couldOpenPosition(depth, previousPosition)) {
//            return;
//        }

        orders.add(new Order(newPosition, this));
    }

    private int computeVolume() {
        if (!position.isNeutral())
            return position.getVolume();

        return portfolio.getLot();
    }

    public double computeClosingCommission(Position newPosition) {
        return commissionStrategy.computeClosePositionCommission(position, newPosition);
    }

    public double computeOpeningCommission(Position newPosition) {
        return commissionStrategy.computeOpenPositionCommission(newPosition);
    }

    public String getDecisionStrategyName() {
        return decisionStrategy.getName();
    }

    public String printDecisionStrategy() {
        return decisionStrategy.printDescription();
    }

    public MoneyState getCurrentState() {
        return new MoneyState(position.getDate(), currentMoney);
    }

    public Candle getLastCandle() {
        return decisionStrategy.getLastCandle();
    }

    public DateTime getPositionDate() {
        return position.getDate();
    }

    public int getPositionVolume() {
        return position.getVolume();
    }

    public String getPortfolioTitle() {
        return portfolio.getTitle();
    }

    public String getSecurity() {
        return portfolio.getSecurity();
    }

    public Direction getPositionDirection() {
        return position.getDirection();
    }

    public void block() {
        isBlocked = true;
    }

    public void unblock() {
        isBlocked = false;
    }

    public int getInitialStorageSize() {
        return decisionStrategy.getInitialStorageSizeFor(depth);
    }

    public double getPositionValue() {
        return position.getValue();
    }

    public int getSignVolume() {
        return position.getSignVolume();
    }
}
