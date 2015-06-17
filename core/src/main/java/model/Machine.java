package model;

import commissionStrategies.ICommissionStrategy;
import decisionStrategies.DecisionStrategy;
import exceptions.PositionAlreadySetFailure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.List;

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
    private boolean isBlocked;

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

    public void addOrderTo(List<Order> orders, List<Candle> candles) {

        Position newPosition = decisionStrategy.computeNewPositionFor(candles, depth, computeVolume());

        if (position.hasSameDirection(newPosition))
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

    public int estimateSufficientCandlesSize() {
        return decisionStrategy.estimateSufficientCandlesSizeFor(depth);
    }
}
