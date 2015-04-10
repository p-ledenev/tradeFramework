package model;

import decisionStrategies.DecisionStrategy;
import exceptions.PositionAlreadySetFailure;
import lombok.Data;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Machine {

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
        currentMoney += position.computeProfit(newPosition);

        position = newPosition;
    }

    private String print() {
        return "depth; " + portfolio.printStrategy();
    }

    public Order processCandles(List<Candle> candles) {
        Position newPosition = decisionStrategy.computeNewPositionFor(candles, position.getVolume());

        if (position.hasSameDirection(newPosition))
            return new EmptyOrder(this);

        return new ExecutableOrder(newPosition, this);
    }

    public State getState() {
        return new State(position.getDate(), currentMoney);
    }

    public Candle getLastCandle() {
        return decisionStrategy.getLastCandle();
    }
}
