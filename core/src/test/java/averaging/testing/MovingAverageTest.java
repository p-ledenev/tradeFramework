package averaging.testing;

import averageConstructors.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by DiKey on 26.04.2015.
 */
public class MovingAverageTest {

    MovingAverageConstructor constructor;

    @Before
    public void setUp() {
        constructor = new MovingAverageConstructor();
    }

    @Test
    public void shouldAverage() {

        double[] y = {4, 8, 9, 12};
        AveragingSupportStub[] values = new AveragingSupportStub[4];

        for (int i = 0; i < values.length; i++)
            values[i] = new AveragingSupportStub(y[i]);

        double result = constructor.average(values);

        assertThat(result, is(equalTo(8.25)));
    }
}
