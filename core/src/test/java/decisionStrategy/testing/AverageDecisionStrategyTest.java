package decisionStrategy.testing;

import averageConstructors.ExponentialAverageConstructor;
import decisionStrategies.AveragingDecisionStrategy;
import model.Candle;
import model.Direction;
import model.Position;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 28.04.2015.
 */
public class AverageDecisionStrategyTest extends DecisionStrategyTestCase<AveragingDecisionStrategy> {

    @Test
    public void shouldComputeOrderDirection() {
        Position position = decisionStrategy.computeNewPositionFor(candles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.buy)));
        assertThat(position.getVolume(), is(equalTo(volume)));
    }

    @Test
    public void shouldComputeDerivative() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
    }

    @Test
    public void shouldComputeAverageDerivative() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        assertThat(getLastAverageDerivative(), is(equalTo(0.1343080830363829)));
    }

    @Test
    public void examineArraysLength() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        assertThat(decisionStrategy.getCandles().size(), is(equalTo(candles.size())));
        assertThat(decisionStrategy.getAverageValues().size(), is(equalTo(candles.size() - depth + 1)));
        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() - depth + 1)));
        assertThat(decisionStrategy.getAverageDerivatives().size(), is(CoreMatchers.equalTo(candles.size() - depth + 1)));
    }

    @Test
    public void shouldAddOneNewCandles() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() + 1 - depth + 1)));
        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
        assertThat(getLastAverageDerivative(), is(equalTo(0.08408860803423382)));
    }

    @Test
    public void shouldAddTwoNewCandles() {
        decisionStrategy.computeNewPositionFor(candles, depth, volume);

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 5.1));

        decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() + 2 - depth + 1)));
        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
        assertThat(getLastAverageDerivative(), is(equalTo(-0.04567186553941528)));
    }

    @Test
    public void shouldReturnNoneDirectionForMinCandlesLength() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 4.5));
        newCandles.add(new Candle(DateTime.now(), 6.0));

        decisionStrategy.setCandles(new ArrayList<Candle>());

        Position position = decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.neutral)));
    }

    @Override
    protected AveragingDecisionStrategy createStrategy() {
        AveragingDecisionStrategy decisionStrategy = new AveragingDecisionStrategy();
        decisionStrategy.setConstructor(new ExponentialAverageConstructor());

        return decisionStrategy;
    }

    private double getLastDerivative() {
        return decisionStrategy.getDerivatives().get(decisionStrategy.getDerivatives().size() - 1).getValue();
    }

    private double computeLastDerivative() {
        double averageValue1 = decisionStrategy.getAverageValues().get(decisionStrategy.getAverageValues().size() - 1);
        double averageValue2 = decisionStrategy.getAverageValues().get(decisionStrategy.getAverageValues().size() - 2);

        return (averageValue1 - averageValue2) / averageValue1;
    }

    public double getLastAverageDerivative() {
        return decisionStrategy.getAverageDerivatives().get(decisionStrategy.getAverageDerivatives().size() - 1);
    }
}
