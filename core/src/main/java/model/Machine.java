package model;

import commissionStrategies.*;
import decisionStrategies.*;
import exceptions.*;
import lombok.*;
import org.joda.time.*;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Getter
@NoArgsConstructor
public class Machine implements IMoneyStateSupport {

    @Setter
    private Portfolio portfolio;
    @Setter
    private Position position;

    private int depth;
    private double currentMoney;
    @Setter
    private boolean isBlocked;

    @Setter
    private DecisionStrategy decisionStrategy;
    @Setter
    private ICommissionStrategy commissionStrategy;

    public Machine(Portfolio portfolio, DecisionStrategy decisionStrategy, ICommissionStrategy commissionStrategy, int depth) {
        this.portfolio = portfolio;
        this.decisionStrategy = decisionStrategy;
        this.commissionStrategy = commissionStrategy;
        this.depth = depth;

        position = Position.begining();
        currentMoney = 0;
        isBlocked = false;
    }

    public Machine(Position position, int depth, double currentMoney, boolean isBlocked) {
        this.position = position;
        this.depth = depth;
        this.currentMoney = currentMoney;
        this.isBlocked = isBlocked;
    }

    public void apply(Position newPosition) throws PositionAlreadySetFailure {

        if (position.hasSameDirection(newPosition))
            throw new PositionAlreadySetFailure("for machine " + print());

        currentMoney -= computeClosingCommission(newPosition);
        currentMoney -= computeOpeningCommission(newPosition);
        currentMoney += position.computeProfit(newPosition.getValue());

        position = newPosition;
    }

    private String print() {
        return "depth; " + portfolio.printStrategy();
    }

    public void addOrderTo(List<Order> orders) {

        Position newPosition = decisionStrategy.computeNewPositionFor(depth, computeVolume());

        if (position.hasSameDirection(newPosition))
            return;

        if (isBlocked)
            return;

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
