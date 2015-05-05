package decisionStrategy.testing;

import decisionStrategies.DecisionStrategy;
import model.Candle;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import siftStrategies.NoSiftStrategy;
import siftStrategies.SiftCandlesStrategy;
import takeProfitStrategies.ITakeProfitStrategy;
import takeProfitStrategies.NoTakeProfitStrategy;

import java.util.ArrayList;
import java.util.List;

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
        for (double value : candleValues)
            candles.add(new Candle(date.plusDays(1), value));

        decisionStrategy = createStrategy();

        SiftCandlesStrategy siftStrategy = new NoSiftStrategy();
        decisionStrategy.setSiftStrategy(siftStrategy);

        ITakeProfitStrategy profitStrategy = new NoTakeProfitStrategy();
        decisionStrategy.setProfitStrategy(profitStrategy);
    }

    @Test
    public void shouldReturnNoneDirectionForOnlyOneCandle() {

        List<Candle> newCandles = new ArrayList<Candle>();
        newCandles.add(new Candle(DateTime.now(), 5.5));

        decisionStrategy.setCandles(new ArrayList<Candle>());

        decisionStrategy.computeNewPositionFor(newCandles, depth, volume);
    }

    protected abstract TStrategy createStrategy();
}
