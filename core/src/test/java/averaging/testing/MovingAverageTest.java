package averaging.testing;

import averageConstructors.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by DiKey on 26.04.2015.
 */
public class MovingAverageTest {

	SimpleAverageConstructor constructor;

	@Before
	public void setUp() {
		constructor = new SimpleAverageConstructor();
	}

	@Test
	public void shouldAverage() {

		double[] y = {4, 8, 9, 12};
		double[] values = new double[4];

		for (int i = 0; i < values.length; i++)
			values[i] = y[i];

		double result = constructor.average(values);

		assertThat(result, is(equalTo(8.25)));
	}
}
