package approximation.testing;

import approximationConstructors.Approximation;
import approximationConstructors.LinearApproximationConstructor;
import org.junit.Before;
import org.junit.Test;
import tools.Log;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ledenev.p on 17.04.2015.
 */

public class OLSLinearApproximationTest {

    LinearApproximationConstructor constructor;

    @Before
    public void setUp() {
        constructor = new LinearApproximationConstructor();
    }

    @Test
    public void shouldApproximate() {

        double[] y = {4, 8, 13, 18};
        ApproximationSupportStub[] values = new ApproximationSupportStub[4];

        for(int i=0; i<values.length; i++)
            values[i] = new ApproximationSupportStub(y[i]);

        Approximation approximation = constructor.approximate(values);

        assertThat(approximation.getRegressionParameters().length, is(equalTo(2)));
        assertThat(approximation.getRegressionParameters()[0], is(equalTo(-1.)));
        assertThat(approximation.getRegressionParameters()[1], is(equalTo(4.7)));
    }
}
