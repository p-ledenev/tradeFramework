package trade.core.fourierConstructors;

import lombok.*;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 25.04.2016.
 */

@AllArgsConstructor
public abstract class Pow2Expander {

	private IPackingValueSupplier valueSupplier;

	public abstract Complex[] expand(Complex[] input);

	protected int nearestPowOfTwo(Complex[] input) {
		int pow2 = 0;
		for (int i = input.length; i > 0; i--) {
			if ((i & (i - 1)) == 0) {
				pow2 = i;
				break;
			}
		}

		return pow2;
	}

	protected Complex getExtraPointValue(Complex[] input) {
		return valueSupplier.compute(input);
	}
}
