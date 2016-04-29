package fourierConstructors;

import lombok.AllArgsConstructor;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by ledenev.p on 27.04.2016.
 */

@AllArgsConstructor
public class FourierTransformConstructor implements IFourierConstructor {

	private int frequenciesNumber;

	@Override
	public Complex[] transform(double[] values) {

		IPackingValueSupplier supplier = new MeanValueSupplier();
		Pow2Expander expander = new TailPow2Expander(supplier);

		Complex[] frequencies = FastFourierTransformer.direct(expander).setInput(values).transform().getResult();
		Complex[] cutFrequencies = HighFrequenciesCutter.create(frequencies).cutAfter(frequenciesNumber);
		Complex[] result = FastFourierTransformer.inverse().setInput(cutFrequencies).transform().getResult();

		return result;
	}
}
