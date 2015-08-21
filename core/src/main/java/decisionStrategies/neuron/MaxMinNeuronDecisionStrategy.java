package decisionStrategies.neuron;

import model.*;
import org.encog.ml.data.*;
import org.encog.ml.data.basic.*;

import java.util.*;

/**
 * Created by ledenev.p on 21.08.2015.
 */
public class MaxMinNeuronDecisionStrategy extends NeuronDecisionStrategy {

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        TrainingResult result = TrainingResult.createFor(Arrays.asList(candles));

        MLData input = new BasicMLData(result.getNormalizedValueIncrementsAsArray());
        MLData output = network.compute(input);

        if (output.getData(0) == 1 && output.getData(1) == 0)
            return Direction.buy;

        if (output.getData(0) == 0 && output.getData(1) == 1)
            return Direction.sell;

        return Direction.neutral;
    }
}
