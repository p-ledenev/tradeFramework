package trade.core.fourierConstructors;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 22.04.2016.
 */

@AllArgsConstructor
public class HighFrequenciesCutter {

	public static HighFrequenciesCutter create(Complex[] input) {
		return new HighFrequenciesCutter(input);
	}

	private Complex[] input;

	public Complex[] cutAfter(int index) {

		Complex[] result = new Complex[input.length];
		for (int i = 0; i < input.length; i++) {
			if (i < index || i >= input.length - index) {
				result[i] = input[i];
			} else {
				result[i] = Complex.ZERO;
			}
		}

		return result;
	}
}
