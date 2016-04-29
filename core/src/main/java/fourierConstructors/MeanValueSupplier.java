package fourierConstructors;

import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 26.04.2016.
 */
public class MeanValueSupplier implements IPackingValueSupplier {

	@Override
	public Complex compute(Complex[] input) {
		double result = 0;
		for (Complex complex : input)
			result += complex.getReal();

		return new Complex(result / input.length);
	}
}
