package decisionStrategies.algorithmic;

import averageConstructors.*;
import decisionStrategies.Strategy;
import lombok.Data;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
@Strategy(name = "SingleAveragingStrategy")
public class SingleAveragingDecisionStrategy extends SingleDerivativeDecisionStrategy {

	private IAverageConstructor constructor;

	public SingleAveragingDecisionStrategy() {
		super();
		constructor = AverageConstructorFactory.createConstructor();
	}

	protected double computeAverageFor(double[] values) {
		return constructor.average(values);
	}
}
