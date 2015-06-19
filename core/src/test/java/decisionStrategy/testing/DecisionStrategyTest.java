package decisionStrategy.testing;

import decisionStrategies.*;
import exceptions.*;
import model.*;
import org.junit.*;
import takeProfitStrategies.*;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public class DecisionStrategyTest {

    @Test
    public void shouldReturnApproximationStrategy() throws Throwable {
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor("ApproximationStrategy", new NoTakeProfitStrategy(), new CandlesStorage());

        assertThat(decisionStrategy, is(instanceOf(ApproximationDecisionStrategy.class)));
    }

    @Test
    public void shouldReturnAveragingStrategy() throws Throwable {
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor("AveragingStrategy", new NoTakeProfitStrategy(), new CandlesStorage());

        assertThat(decisionStrategy, is(instanceOf(AveragingDecisionStrategy.class)));
    }

    @Test
    public void shouldDoNotFoundStrategy() throws Throwable {
        try {
            DecisionStrategy decisionStrategy = DecisionStrategy.createFor("NotFoundStrategy", new NoTakeProfitStrategy(), new CandlesStorage());
            assertTrue(false);

        } catch (NoDecisionStrategyFoundFailure e) {
        }
    }
}
