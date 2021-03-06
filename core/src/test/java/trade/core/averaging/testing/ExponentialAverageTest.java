package trade.core.averaging.testing;

import trade.core.averageConstructors.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by DiKey on 26.04.2015.
 */
public class ExponentialAverageTest {

	ExponentialAverageConstructor constructor;

	@Before
	public void setUp() {
		constructor = new ExponentialAverageConstructor();
	}

	@Test
	public void shouldAverage() {

		double[] y = {4, 8, 9, 12};
		double[] values = new double[y.length];

		for (int i = 0; i < values.length; i++)
			values[i] = y[i];

		double result = constructor.average(values);

		assertThat(result, is(equalTo(11.95833333333333)));
	}

	@Test
	public void shouldAverage2() {

		double[] y = {3.7, 4.5, 5};
		double[] values = new double[y.length];

		for (int i = 0; i < values.length; i++)
			values[i] = y[i];

		double result = constructor.average(values);

		assertThat(result, is(equalTo(5.033333333333332)));
	}

	@Test
	public void shouldAverage3() {

		double[] y = {0.065660093, 0.160399702, 0.119206087};
		double[] values = new double[y.length];

		for (int i = 0; i < values.length; i++)
			values[i] = y[i];

		double result = constructor.average(values);

		assertThat(result, is(equalTo(0.13430977855555554)));
	}
}
