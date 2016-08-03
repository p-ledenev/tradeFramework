package trade.core.fourierConstructors;

import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 27.04.2016.
 */
public class EmptyExpander extends Pow2Expander {

	public EmptyExpander() {
		super(null);
	}

	@Override
	public Complex[] expand(Complex[] input) {
		return input;
	}
}
