package trade.core.decisionStrategies.neuronTraining;

import trade.core.approximationConstructors.*;
import trade.core.decisionStrategies.Strategy;
import trade.core.model.*;
import trade.core.tools.Format;

import java.util.List;

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

		ap = constructor.approximate(Format.toDoubleArray(data));
		double highestDegreeParam = ap.getHighestDegreeParameter();

		Candle current = candlesStorage.last();
		Candle future = data.get(data.size() - 1);

		if (highestDegreeParam > 0)
			return Direction.Buy;

		if (highestDegreeParam < 0)
			return Direction.Sell;

		return Direction.Neutral;
	}
}
