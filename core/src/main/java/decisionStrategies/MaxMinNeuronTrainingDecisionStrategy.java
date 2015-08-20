package decisionStrategies;

import lombok.*;
import model.*;

import java.util.*;

/**
 * Created by ledenev.p on 04.08.2015.
 */

@Strategy(name = "NeuroTrainingStrategy")
public class NeuroTrainingDecisionStrategy extends DecisionStrategy {

    @Setter
    CandlesStorage allDataStorage;

    private double rise;
    private double fall;

    @Override
    protected Direction computeOrderDirection(int depth) {
        Candle last = candlesStorage.last();

        List<Candle> data = allDataStorage.getAfter(last, depth);

        if (data.size() < depth)
            return Direction.neutral;

        return computeDirection(data);
    }

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
    public int getInitialStorageSizeFor(int depth) {
        return depth;
    }

    private Direction computeDirection(List<Candle> data) {

        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
        for (Candle candle : data) {
            max = candle.getValue() > max ? candle.getValue() : max;
            min = candle.getValue() < min ? candle.getValue() : min;
        }

        double last = candlesStorage.last().getValue();

        rise = (max - last) / last;
        fall = (last - min) / last;

        //Log.info("rise: " + rise + "; fall: " + fall);

        if (rise > 0.001)
            return Direction.buy;

        if (fall > 0.001)
            return Direction.sell;

        return Direction.neutral;
    }
}
