package decisionStrategy.testing;

import decisionStrategies.ApproximationDecisionStrategy;
import decisionStrategies.AveragingDecisionStrategy;
import decisionStrategies.DecisionStrategy;
import exceptions.NoDecisionStrategyFoundFailure;
import org.junit.Test;
import siftStrategies.NoSiftStrategy;
import takeProfitStrategies.NoTakeProfitStrategy;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public class DecisionStrategyTest {

    @Test
    public void shouldReturnApproximationStrategy() throws Throwable {
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor("ApproximationStrategy", new NoTakeProfitStrategy(), new NoSiftStrategy());

        assertThat(decisionStrategy, is(instanceOf(ApproximationDecisionStrategy.class)));
    }

    @Test
    public void shouldReturnAveragingStrategy() throws Throwable {
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor("AveragingStrategy", new NoTakeProfitStrategy(), new NoSiftStrategy());

        assertThat(decisionStrategy, is(instanceOf(AveragingDecisionStrategy.class)));
    }

    @Test
    public void shouldDoNotFoundStrategy() throws Throwable {
        try {
            DecisionStrategy decisionStrategy = DecisionStrategy.createFor("NotFoundStrategy", new NoTakeProfitStrategy(), new NoSiftStrategy());
            assertTrue(false);

        } catch (NoDecisionStrategyFoundFailure e) {
        }
    }
}
