package trade.core.fourierConstructors;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 27.04.2016.
 */

@AllArgsConstructor
public class FourierTransformConstructor implements IFourierConstructor {

	private int frequenciesNumber;
	private Pow2Expander expander;

	@Override
	public Complex[] transform(double[] values) {

		Complex[] frequencies = FastFourierTransformer.direct(expander).setInput(values).transform().getResult();
		Complex[] cutFrequencies = HighFrequenciesCutter.create(frequencies).cutAfter(frequenciesNumber);
		Complex[] result = FastFourierTransformer.inverse().setInput(cutFrequencies).transform().getResult();

		return result;
	}
}
