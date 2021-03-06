package trade.core.decisionStrategies.algorithmic;

import trade.core.approximationConstructors.*;
import trade.core.decisionStrategies.*;
import lombok.*;
import trade.core.model.*;
import trade.core.tools.Format;

/**
 * Created by ledenev.p on 05.05.2015.
 */

@Getter @Setter
@Strategy(name = "ApproximationStrategy")
public class ApproximationDecisionStrategy extends DecisionStrategy {

	private IApproximationConstructor constructor;
	private Approximation ap;

	public ApproximationDecisionStrategy() {
		constructor = new LinearApproximationConstructor();
	}

	@Override
	public Direction computeOrderDirection(Candle[] candles) {

		if (candlesStorage.size() < candles.length)
			return Direction.Neutral;

		ap = constructor.approximate(Format.toDoubleArray(candles));

		double highestDegreeParam = ap.getHighestDegreeParameter();

		if (highestDegreeParam > 0)
			return Direction.Buy;

		if (highestDegreeParam < 0)
			return Direction.Sell;

		return Direction.Neutral;
	}

	@Override
	public String[] getStateParamsHeader() {
		return new String[]{
				"k", "kx+b", "approximatedValue"
		};
	}

	@Override
	protected String[] collectCurrentStateParams() {
		return new String[]{
				Double.toString(ap.getHighestDegreeParameter()),
				ap.printPowerFunction(),
				Double.toString(ap.computeApproximatedValue())
		};
	}

	@Override
	public int getInitialStorageSizeFor(int depth) {
		return depth + 2;
	}

	@Override
	public boolean hasCurrentState() {
		return ap != null;
	}
}
