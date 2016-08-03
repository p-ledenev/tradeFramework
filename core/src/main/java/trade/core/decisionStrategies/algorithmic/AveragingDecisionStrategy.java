package trade.core.decisionStrategies.algorithmic;

import trade.core.averageConstructors.*;
import trade.core.decisionStrategies.Strategy;
import lombok.*;

/**
 * Created by DiKey on 12.04.2015.
 */

@Getter @Setter
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
