package fourierConstructors;

import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 25.04.2016.
 */
public class TailPow2Expander extends Pow2Expander {

	public TailPow2Expander(IPackingValueSupplier valueSupplier) {
		super(valueSupplier);
	}

	@Override
	public Complex[] expand(Complex[] input) {
		int pow2 = nearestPowOfTwo(input);

		Complex[] result = new Complex[2 * pow2];
		for (int i = 0; i < result.length; i++)
			result[i] = getExtraPointValue(input);

		for (int i = 0; i < input.length; i++)
			result[i] = input[i];

		return result;
	}
}
