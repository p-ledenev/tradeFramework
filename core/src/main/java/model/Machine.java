package model;

import commissionStrategies.ICommissionStrategy;
import decisionStrategies.DecisionStrategy;
import exceptions.PositionAlreadySetFailure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Machine implements IMoneyStateSupport {

    private Portfolio portfolio;

    private int depth;
    private double currentMoney;
    private Position position;
    DecisionStrategy decisionStrategy;
    private ICommissionStrategy commissionStrategy;

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
        Position newPosition = decisionStrategy.computeNewPositionFor(candles, depth, position.getVolume());

        if (position.hasSameDirection(newPosition))
            return new EmptyOrder(this);

        return new ExecutableOrder(newPosition, this);
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

    public List<Candle> getCandles() {
        return decisionStrategy.getCandles();
    }
}
