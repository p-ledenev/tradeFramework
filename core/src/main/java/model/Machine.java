package model;

import decisionStrategies.DecisionStrategy;
import exceptions.PositionAlreadySetFailure;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Machine implements IStateSupport {

    private Portfolio portfolio;

    private int depth;
    private double currentMoney;
    private Position position;
    DecisionStrategy decisionStrategy;

    public void apply(Position newPosition) throws PositionAlreadySetFailure {

        if (position.hasSameDirection(newPosition))
            throw new PositionAlreadySetFailure("for machine " + print());

        currentMoney -= position.computeClosingCommission(newPosition);
        currentMoney -= newPosition.computeOpeningCommission();
        currentMoney += position.computeProfit(newPosition.getValue());

        position = newPosition;
    }

    private String print() {
        return "depth; " + portfolio.printStrategy();
    }

    public Order processCandles(List<Candle> candles) {
        Position newPosition = decisionStrategy.computeNewPositionFor(candles, depth, position.getVolume());

        if (position.hasSameDirection(newPosition))
            return new EmptyOrder(this);

        return new ExecutableOrder(newPosition, this);
    }

    public String getDecisionStrategyName() {
        return decisionStrategy.getName();
    }

    public State getState() {
        return new State(position.getDate(), currentMoney);
    }

    public Candle getLastCandle() {
        return decisionStrategy.getLastCandle();
    }

    public DateTime getPositionDate() {
        return position.getDate();
    }

    public List<Candle> getCandles() {
        return decisionStrategy.getCandles();
    }
}
