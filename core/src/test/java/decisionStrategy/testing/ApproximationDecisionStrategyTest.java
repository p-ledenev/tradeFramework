package decisionStrategy.testing;

import approximationConstructors.LinearApproximationConstructor;
import decisionStrategies.ApproximationDecisionStrategy;
import model.Candle;
import model.OrderDirection;
import model.Position;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public class ApproximationDecisionStrategyTest extends DecisionStrategyTestCase<ApproximationDecisionStrategy> {

    @Test
    public void shouldComputeOrderDirection() {
        Position position = decisionStrategy.computeNewPositionFor(candles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(OrderDirection.buy)));
        assertThat(position.getVolume(), is(equalTo(volume)));
    }

    @Test
    public void shouldAddOneNewCandles() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        Position position = decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(OrderDirection.buy)));
    }

    @Test
    public void shouldAddTwoNewCandles() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 5.1));

        Position position = decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(OrderDirection.buy)));
    }

    @Test
    public void shouldReturnPositionForMinCandlesLength() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 4.5));
        newCandles.add(new Candle(DateTime.now(), 3.0));

        decisionStrategy.setCandles(new ArrayList<Candle>());

        Position position = decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(OrderDirection.sell)));
    }

    @Override
    protected ApproximationDecisionStrategy createStrategy() {
        ApproximationDecisionStrategy strategy = new ApproximationDecisionStrategy();
        strategy.setConstructor(new LinearApproximationConstructor());

        return strategy;
    }
}
