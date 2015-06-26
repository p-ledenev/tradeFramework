package decisionStrategy.testing;

import approximationConstructors.*;
import decisionStrategies.*;
import model.*;
import org.joda.time.*;
import org.junit.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public class ApproximationDecisionStrategyTest extends DecisionStrategyTestCase<ApproximationDecisionStrategy> {

    @Test
    public void shouldComputeOrderDirection() {
        Position position = decisionStrategy.computeNewPositionFor(depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.buy)));
        assertThat(position.getVolume(), is(equalTo(volume)));
    }

    @Test
    public void shouldAddOneNewCandles() {
        decisionStrategy.computeNewPositionFor(depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        decisionStrategy.getCandlesStorage().add(newCandles);
        Position position = decisionStrategy.computeNewPositionFor(depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.buy)));
    }

    @Test
    public void shouldAddTwoNewCandles() {
        decisionStrategy.computeNewPositionFor(depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 5.1));

        decisionStrategy.getCandlesStorage().add(newCandles);
        Position position = decisionStrategy.computeNewPositionFor(depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.buy)));
    }

    @Test
    public void shouldReturnPositionForMinCandlesLength() {

        decisionStrategy.getCandlesStorage().setCandles(new ArrayList<Candle>());

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 4.5));
        newCandles.add(new Candle(DateTime.now(), 3.0));

        decisionStrategy.getCandlesStorage().add(newCandles);
        Position position = decisionStrategy.computeNewPositionFor(depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.sell)));
    }

    @Override
    protected ApproximationDecisionStrategy createStrategy() {
        ApproximationDecisionStrategy strategy = new ApproximationDecisionStrategy();
        strategy.setConstructor(new LinearApproximationConstructor());

        return strategy;
    }
}
