package trade.core.decisionStrategy.testing;

import org.joda.time.DateTime;
import org.junit.*;
import trade.core.decisionStrategies.DecisionStrategy;
import trade.core.model.*;
import trade.core.siftStrategies.NoSiftStrategy;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public abstract class DecisionStrategyTestCase<TStrategy extends DecisionStrategy> {

    TStrategy decisionStrategy;
    List<Candle> candles;

    int depth;
    int volume;

    @Before
    public void setUp() {

        double[] candleValues = {1, 1.5, 2.1, 4, 3.5, 2.5, 2.9, 3.1, 3.5, 3.7, 4.5, 5};
        DateTime date = DateTime.now();

        depth = 3;
        volume = 10;

        candles = new ArrayList<Candle>();
        List<Candle> candlesForStorage = new ArrayList<Candle>();
        for (double value : candleValues) {
            candles.add(new Candle(date.plusDays(1), value));
            candlesForStorage.add(new Candle(date.plusDays(1), value));
        }

        decisionStrategy = createStrategy();

        decisionStrategy.setCandlesStorage(new CandlesStorage(new NoSiftStrategy(), candlesForStorage));
    }

    @Test
    public void shouldReturnNeutralDirectionForOnlyOneCandle() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        decisionStrategy.getCandlesStorage().setCandles(newCandles);

        Position position = decisionStrategy.computeNewPositionFor(depth, volume);

        assertThat(position.getDirection(), is(equalTo(Direction.Neutral)));
    }

    protected abstract TStrategy createStrategy();
}
