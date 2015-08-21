package decisionStrategies.neuron;

import approximationConstructors.*;
import decisionStrategies.*;
import model.*;

import java.util.*;

/**
 * Created by ledenev.p on 04.08.2015.
 */

@Strategy(name = "ApproximationNeuronTrainingStrategy")
public class ApproximationNeuronTrainingDecisionStrategy extends NeuronTrainingDecisionStrategy {

    private IApproximationConstructor constructor;
    private Approximation ap;

    public ApproximationNeuronTrainingDecisionStrategy() {
        constructor = new LinearApproximationConstructor();
    }

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                "k",
                "approximatedValue"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString(ap.getHighestDegreeParameter()),
                Double.toString(ap.computeApproximatedValue())
        };
    }

    @Override
    protected Direction computeDirection(List<Candle> data) {

        ap = constructor.approximate(data.toArray(new Candle[data.size()]));
        double highestDegreeParam = ap.getHighestDegreeParameter();

        Candle current = candlesStorage.last();
        Candle future = data.get(data.size() - 1);

        if (highestDegreeParam > 0 && future.greaterThan(current))
            return Direction.buy;

        if (highestDegreeParam < 0 && future.lessThan(current))
            return Direction.sell;

        return Direction.neutral;
    }
}
