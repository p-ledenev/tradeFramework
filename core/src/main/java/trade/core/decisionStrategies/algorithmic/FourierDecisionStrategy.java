package trade.core.decisionStrategies.algorithmic;

import trade.core.decisionStrategies.Strategy;
import trade.core.fourierConstructors.*;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by DiKey on 12.04.2015.
 */

@Getter @Setter
@Strategy(name = "FourierStrategy")
public class FourierDecisionStrategy extends DerivativeDecisionStrategy {

	private IFourierConstructor constructor;

	public FourierDecisionStrategy(int frequency) {
		super();
		constructor = FourierConstructorFactory.createConstructor(frequency);
	}

	public FourierDecisionStrategy(int fastFrequency, int slowFrequency) {
		super();
		constructor = FourierConstructorFactory.createSEMAConstructor(fastFrequency, slowFrequency);
	}

	public FourierDecisionStrategy() {
		super();
		constructor = FourierConstructorFactory.createSEMAConstructor(4, 1);
	}

	@Override
	protected double computeAverageFor(double[] values) {
		Complex[] result = constructor.transform(values);
		return result[values.length - 1].getReal();
	}
}