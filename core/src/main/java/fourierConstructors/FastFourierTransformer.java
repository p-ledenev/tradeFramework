package fourierConstructors;

import lombok.Getter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.*;

/**
 * Created by ledenev.p on 21.04.2016.
 */

public abstract class FastFourierTransformer {

	public static FastFourierTransformer direct(Pow2Expander expander) {
		return new DirectFourierTransform(expander);
	}

	public static FastFourierTransformer inverse() {
		return new InverseFourierTransform(new EmptyExpander());
	}

	protected Complex[] input;
	@Getter
	protected Complex[] result;

	private Pow2Expander expander;


	public FastFourierTransformer(Pow2Expander expander) {
		input = new Complex[1];
		result = new Complex[1];

		this.expander = expander;
	}

	public FastFourierTransformer setInput(Complex[] input) {
		this.input = input;
		return this;
	}

	public FastFourierTransformer setInput(double[] input) {

		this.input = new Complex[input.length];
		for (int i = 0; i < input.length; i++)
			this.input[i] = new Complex(input[i]);

		return this;
	}

	public FastFourierTransformer transform() {
		if (input.length < 2)
			throw new RuntimeException("Input data size too small");

		org.apache.commons.math3.transform.FastFourierTransformer transformer =
				new org.apache.commons.math3.transform.FastFourierTransformer(DftNormalization.STANDARD);

		Complex[] newInput = expander.expand(input);
		result = transformer.transform(newInput, getType());

		return this;
	}

	protected abstract TransformType getType();
}
