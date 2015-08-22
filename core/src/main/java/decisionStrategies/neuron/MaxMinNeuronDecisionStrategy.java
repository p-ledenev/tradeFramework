package decisionStrategies.neuron;

import decisionStrategies.*;
import model.*;
import org.encog.ml.data.*;
import org.encog.ml.data.basic.*;

import java.util.*;

/**
 * Created by ledenev.p on 21.08.2015.
 */

@Strategy(name = "MaxMinNeuronStrategy")
public class MaxMinNeuronDecisionStrategy extends NeuronDecisionStrategy {

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        TrainingResult result = TrainingResult.createFor(Arrays.asList(candles));

        MLData input = new BasicMLData(result.getNormalizedValueIncrementsAsArray());
        MLData output = network.compute(input);

        if (output.getData(0) > output.getData(1))
            return Direction.buy;

        return Direction.sell;
    }

    @Override
    public String getNetworkName() {
        return "Perceptron";
    }
}
