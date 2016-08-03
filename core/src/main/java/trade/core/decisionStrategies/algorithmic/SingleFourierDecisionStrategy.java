package trade.core.decisionStrategies.algorithmic;

import trade.core.decisionStrategies.Strategy;
import trade.core.fourierConstructors.*;
import lombok.*;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by DiKey on 12.04.2015.
 */

@Getter @Setter
@Strategy(name = "SingleFourierStrategy")
public class SingleFourierDecisionStrategy extends SingleDerivativeDecisionStrategy {

	private IFourierConstructor constructor;

	public SingleFourierDecisionStrategy(int frequency) {
		super();
		constructor = FourierConstructorFactory.createConstructor(frequency);
	}

	public SingleFourierDecisionStrategy(int fastFrequency, int slowFrequency) {
		super();
		constructor = FourierConstructorFactory.createSEMAConstructor(fastFrequency, slowFrequency);
	}

	public SingleFourierDecisionStrategy() {
		super();
		constructor = FourierConstructorFactory.createSEMAConstructor(4, 1);
	}

	@Override
	protected double computeAverageFor(double[] values) {
		Complex[] result = constructor.transform(values);
		return result[values.length - 1].getReal();
	}
}