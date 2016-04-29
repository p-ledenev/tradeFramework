package fourierConstructors;

import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 26.04.2016.
 */
public class LastValueSupplier implements IPackingValueSupplier {

	@Override
	public Complex compute(Complex[] input) {
		return input[input.length - 1];
	}
}
