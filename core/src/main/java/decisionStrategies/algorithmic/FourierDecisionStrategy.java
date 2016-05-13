package decisionStrategies.algorithmic;

import decisionStrategies.Strategy;
import fourierConstructors.*;
import lombok.Data;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
@Strategy(name = "FourierStrategy")
public class FourierDecisionStrategy extends DerivativeDecisionStrategy {

	private IFourierConstructor constructor;

	public FourierDecisionStrategy(int frequency) {
		super();
		constructor = FourierConstructorFactory.createConstructor(frequency);
	}

	public FourierDecisionStrategy() {
		super();
		constructor = FourierConstructorFactory.createConstructor(1);
	}

	@Override
	protected double computeAverageFor(double[] values) {
		Complex[] result = constructor.transform(values);
		return result[values.length - 1].getReal();
	}
}