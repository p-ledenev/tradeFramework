package averaging.testing;

import decisionStrategies.AveragingDecisionStrategy;
import model.Candle;
import model.OrderDirection;
import model.Position;
import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import siftStrategies.NoSiftStrategy;
import siftStrategies.SiftCandlesStrategy;
import takeProfitStrategies.ITakeProfitStrategy;
import takeProfitStrategies.NoTakeProfitStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 28.04.2015.
 */
public class AverageDecisionStrategyTest {

    AveragingDecisionStrategy decisionStrategy;
    List<Candle> candles;
    Position position;

    int depth;
    int volume;

    @Before
    public void setUp() {

        double[] candleValues = {1, 1.5, 2.1, 4, 3.5, 2.5, 2.9, 3.1, 3.5, 3.7, 4.5, 5};
        DateTime date = DateTime.now();

        depth = 3;
        volume = 10;

        candles = new ArrayList<Candle>();
        for (double value : candleValues)
            candles.add(new Candle(date.plusDays(1), value));

        decisionStrategy = new AveragingDecisionStrategy();

        SiftCandlesStrategy siftStrategy = new NoSiftStrategy();
        decisionStrategy.setSiftStrategy(siftStrategy);

        ITakeProfitStrategy profitStrategy = new NoTakeProfitStrategy();
        decisionStrategy.setProfitStrategy(profitStrategy);

        position = decisionStrategy.computeNewPositionFor(candles, depth, volume);
    }

    @Test
    public void shouldComputeOrderDirection() {
        assertThat(position.getDirection(), is(equalTo(OrderDirection.buy)));
        assertThat(position.getVolume(), is(equalTo(volume)));
    }

    @Test
    public void shouldComputeDerivative() {
        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
    }

    @Test
    public void shouldComputeAverageDerivative() {
        assertThat(getLastAverageDerivative(), is(equalTo(0.1343)));
    }

    @Test
    public void examineArraysLength() {
        assertThat(decisionStrategy.getCandles().size(), is(equalTo(candles.size())));
        assertThat(decisionStrategy.getAverageValues().size(), is(equalTo(candles.size() - depth + 1)));
        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() - depth + 1)));
        assertThat(decisionStrategy.getAverageDerivatives().size(), is(CoreMatchers.equalTo(candles.size() - depth + 1)));
    }

    @Test
    public void shouldAddOneNewCandles() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() + 1 - depth + 1)));
        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
        assertThat(getLastAverageDerivative(), is(equalTo(0.0841)));
    }

    @Test
    public void shouldAddTwoNewCandles() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));
        newCandles.add(new Candle(DateTime.now(), 5.1));

        decisionStrategy.computeNewPositionFor(newCandles, depth, volume);

        assertThat(decisionStrategy.getDerivatives().size(), is(CoreMatchers.equalTo(candles.size() + 2 - depth + 1)));
        assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
        assertThat(getLastAverageDerivative(), is(equalTo(-0.0457)));
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
