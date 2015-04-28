package averaging.testing;

import averageConstructors.ExponentialAverageConstructor;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        AveragingSupportStub[] values = new AveragingSupportStub[y.length];

        for (int i = 0; i < values.length; i++)
            values[i] = new AveragingSupportStub(y[i]);

        double result = constructor.average(values);

        assertThat(result, is(equalTo(11.9583)));
    }

    @Test
    public void shouldAverage2() {

        double[] y = {3.7, 4.5, 5};
        AveragingSupportStub[] values = new AveragingSupportStub[y.length];

        for (int i = 0; i < values.length; i++)
            values[i] = new AveragingSupportStub(y[i]);

        double result = constructor.average(values);

        assertThat(result, is(equalTo(5.0333)));
    }

    @Test
    public void shouldAverage3() {

        double[] y = {0.065660093, 0.160399702, 0.119206087};
        AveragingSupportStub[] values = new AveragingSupportStub[y.length];

        for (
                int i = 0;
                i < values.length; i++)
            values[i] = new

                    AveragingSupportStub(y[i]);

        double result = constructor.average(values);

        assertThat(result, is(equalTo(0.1343)));
    }
}
