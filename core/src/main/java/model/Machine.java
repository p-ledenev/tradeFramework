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

    public Order processCandles(List<Candle> candles) {

        Position newPosition = decisionStrategy.computeNewPositionFor(candles, depth, computeVolume());

        if (position.hasSameDirection(newPosition))
            return new EmptyOrder(this);

        return new ExecutableOrder(newPosition, this);
    }

    private int computeVolume() {
        if (!position.isNone())
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

    public DateTime getLastCandleDate() {
        return decisionStrategy.getLastCandleDate();
    }

    public DateTime getPositionDate() {
        return position.getDate();
    }
}
