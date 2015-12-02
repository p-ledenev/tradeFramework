package model.testing;

import model.*;
import org.junit.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 12.11.2015.
 */
public class TrainingResultTest {

    private List<Double> values;

    @Before
    public void setUp() {
        values = new ArrayList<Double>() {
            {
                add(0.04);
                add(11.4);
                add(6.2);
            }
        };
    }

    @Test
    public void shouldNormalize() {

        List<Double> normalized = TrainingResult.round(TrainingResult.normalize(values));

        assertThat(normalized.get(0), is(equalTo(-0.8)));
        assertThat(normalized.get(1), is(equalTo(0.8)));
        assertThat(normalized.get(2), is(equalTo(0.0676)));
    }

    @Test
    public void shouldCentralize() {

        List<Double> centralized = TrainingResult.round(TrainingResult.centralize(values));

        assertThat(centralized.get(0), is(equalTo(-1.2577)));
        assertThat(centralized.get(1), is(equalTo(1.1888)));
        assertThat(centralized.get(2), is(equalTo(0.0689)));
    }

    @Test
    public void shouldCentralizeAndNormalize() {

        List<Double> normalized = TrainingResult.round(TrainingResult.normalize(TrainingResult.centralize(values)));

        assertThat(normalized.get(0), is(equalTo(-0.8)));
        assertThat(normalized.get(1), is(equalTo(0.8)));
        assertThat(normalized.get(2), is(equalTo(0.0676)));
    }

    @Test
    public void shouldNormalizeAndCentralize() {

        List<Double> centralized = TrainingResult.round(TrainingResult.centralize(TrainingResult.normalize(values)));

        assertThat(centralized.get(0), is(equalTo(-1.2577)));
        assertThat(centralized.get(1), is(equalTo(1.1888)));
        assertThat(centralized.get(2), is(equalTo(0.0689)));
    }

    @Test
    public void shouldNormalizeAndCentralizeAndNormalize() {

        List<Double> normalized = TrainingResult.round(TrainingResult.normalize(TrainingResult.centralize(TrainingResult.normalize(values))));

        assertThat(normalized.get(0), is(equalTo(-0.8)));
        assertThat(normalized.get(1), is(equalTo(0.8)));
        assertThat(normalized.get(2), is(equalTo(0.0676)));
    }
}
