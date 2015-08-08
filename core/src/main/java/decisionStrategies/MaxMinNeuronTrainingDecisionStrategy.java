package decisionStrategies;

import model.*;

import java.util.*;

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
                "rise",
                "fall"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString(rise),
                Double.toString(fall)
        };
    }

    @Override
    protected Direction computeDirection(List<Candle> data) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (Candle candle : data) {
            max = candle.getValue() > max ? candle.getValue() : max;
            min = candle.getValue() < min ? candle.getValue() : min;
        }

        double last = candlesStorage.last().getValue();

        rise = (max - last) / last;
        fall = (last - min) / last;

        double kf = 0;

        if (rise < kf && fall < kf)
            return Direction.neutral;

        if (rise > fall)
            return Direction.buy;
        else
            return Direction.sell;
    }
}
