package fourier.testing;

import fourierConstructors.FourierTransformConstructor;
import org.apache.commons.math3.complex.Complex;
import org.junit.*;
import tools.Round;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by DiKey on 26.04.2015.
 */
public class FourierTransformTest {

	FourierTransformConstructor constructor;

	@Before
	public void setUp() {
		constructor = new FourierTransformConstructor(5);
	}

	@Test
	public void shouldTransform() throws Throwable {

		Complex[] input = InputDataReader.create().readFrom("sin(x)+0.2(sin10x).txt").getData();
		double[] values = new double[input.length];

		for (int i = 0; i < values.length; i++)
			values[i] = input[i].getReal();

		double result = Round.toSignificant(constructor.transform(values)[input.length - 1].getReal());

		assertThat(result, is(equalTo(-0.5285)));
	}
}
