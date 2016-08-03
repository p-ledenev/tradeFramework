package trade.core.decisionStrategies.neuronTraining;

import trade.core.decisionStrategies.Strategy;
import trade.core.model.*;
import trade.core.tools.Round;

import java.util.List;

/**
 * Created by ledenev.p on 04.08.2015.
 */

@Strategy(name = "StatisticNeuronTrainingStrategy")
public class StatisticNeuronTrainingDecisionStrategy extends NeuronTrainingDecisionStrategy {

    private double mean = 0;
    private double sigma = 0;

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                "mean",
                "sigma"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString(Round.toDecadeAmount(mean)),
                Double.toString(Round.toDecadeAmount(sigma))
        };
    }

    @Override
    public Direction computeDirection(List<Candle> data) {

        double meanS = 0;
        for (Candle candle : data) {
            mean += candle.getValue();
          //  meanS += Math.pow(candle.getValue(), 2);
        }

        mean /= (double) data.size();
       // meanS /= (double) data.size();

       // sigma = Math.pow(meanS - Math.pow(mean, 2), 0.5);

        double last = candlesStorage.last().getValue();

        //Log.info("mean: " + mean + "; sigma: " + sigma);

        double profit = 0.0025;
        double deviation = (mean - last) / last;
        if (Math.abs(deviation) < profit)
            return Direction.Neutral;

        return (deviation > 0) ? Direction.Buy : Direction.Sell;
    }
}
