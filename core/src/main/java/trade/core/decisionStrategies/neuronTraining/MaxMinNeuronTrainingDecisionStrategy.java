package trade.core.decisionStrategies.neuronTraining;

import trade.core.decisionStrategies.Strategy;
import trade.core.model.*;
import trade.core.tools.Round;

import java.util.List;

/**
 * Created by ledenev.p on 04.08.2015.
 */

@Strategy(name = "MaxMinNeuronTrainingStrategy")
public class MaxMinNeuronTrainingDecisionStrategy extends NeuronTrainingDecisionStrategy {

    private double rise;
    private double fall;

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                "averageDerivative",
                "zero"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString(Round.toAmount(averagingStrategy.getLastAverageValue(), 0)),
                ""
        };
    }

    @Override
    public Direction computeDirection(List<Candle> data) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (Candle candle : data) {
            max = candle.getValue() > max ? candle.getValue() : max;
            min = candle.getValue() < min ? candle.getValue() : min;
        }

        double last = candlesStorage.last().getValue();

        rise = (max - last) / last;
        fall = (last - min) / last;

        //Log.info("rise: " + rise + "; fall: " + fall);

        double profit = 0.004;
        if (rise < profit && fall < profit)
            return Direction.Hold;

        return (rise > fall) ? Direction.Buy : Direction.Sell;
    }
}
