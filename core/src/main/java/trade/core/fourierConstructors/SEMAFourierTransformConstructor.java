package trade.core.fourierConstructors;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 27.04.2016.
 */

@AllArgsConstructor
public class SEMAFourierTransformConstructor implements IFourierConstructor {

	private int slowFrequenciesNumber;
	private int fastFrequenciesNumber;
	private Pow2Expander expander;

	@Override
	public Complex[] transform(double[] values) {

		Complex[] frequencies = FastFourierTransformer.direct(expander).setInput(values).transform().getResult();
		Complex[] fast = backTransform(frequencies, fastFrequenciesNumber);
		Complex[] slow = backTransform(frequencies, slowFrequenciesNumber);

		Complex[] result = new Complex[fast.length];
		for (int i = 0; i < result.length; i++)
			result[i] = createWith(fast[i], slow[i]);

		return result;
	}

	private Complex[] backTransform(Complex[] frequencies, int frequenciesNumber) {
		Complex[] cutFrequencies = HighFrequenciesCutter.create(frequencies).cutAfter(frequenciesNumber);
		Complex[] result = FastFourierTransformer.inverse().setInput(cutFrequencies).transform().getResult();

		return result;
	}

	private Complex createWith(Complex fast, Complex slow) {
		double real = 2 * fast.getReal() - slow.getReal();
		double image = 2 * fast.getImaginary() - slow.getImaginary();

		return new Complex(real, image);
	}
}
