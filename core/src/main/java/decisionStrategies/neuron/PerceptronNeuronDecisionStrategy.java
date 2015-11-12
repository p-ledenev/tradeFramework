package decisionStrategies.neuron;

import decisionStrategies.*;
import model.*;
import org.encog.ml.data.*;
import org.encog.ml.data.basic.*;

import java.util.*;

/**
 * Created by ledenev.p on 21.08.2015.
 */

@Strategy(name = "PerceptronNeuronStrategy")
public class PerceptronNeuronDecisionStrategy extends NeuronDecisionStrategy {

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        TrainingResult result = createTrainingSet(candles);

        if (result.incrementsLength() < candles.length - 1)
            return Direction.neutral;

        MLData input = new BasicMLData(result.getNormalizedValueIncrementsAsArray());
        MLData output = network.compute(input);

        double outputValue = 0.7;
//        if (Math.abs(output.getData(0) - output.getData(1)) < outputValue)
//            return Direction.neutral;

        if (output.getData(0) > outputValue)
            return Direction.buy;

        if (output.getData(1) > outputValue)
            return Direction.sell;

        return Direction.hold;
    }

    @Override
    public String getNetworkName() {
        return "Perceptron";
    }

    private TrainingResult createTrainingSet(Candle[] candles) {

        averagingStrategy.computeNewPositionFor(candles.length, 1);
        List<Double> averages = averagingStrategy.getAverageValues(candles.length);

        return TrainingResult.createFor(averages, Direction.neutral);
        //return TrainingResult.createForCandles(Arrays.asList(candles));
    }
}