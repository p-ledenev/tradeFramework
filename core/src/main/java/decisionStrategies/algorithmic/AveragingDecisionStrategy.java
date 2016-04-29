package decisionStrategies.algorithmic;

import averageConstructors.*;
import decisionStrategies.Strategy;
import lombok.Data;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
@Strategy(name = "AveragingStrategy")
public class AveragingDecisionStrategy extends DerivativeDecisionStrategy {

	private IAverageConstructor constructor;

	public AveragingDecisionStrategy() {
		super();
		constructor = AverageConstructorFactory.createConstructor();
	}

	protected double computeAverageFor(double[] values) {
		return constructor.average(values);
	}
}
