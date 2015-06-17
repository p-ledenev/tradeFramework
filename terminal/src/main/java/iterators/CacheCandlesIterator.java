package iterators;

import model.*;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */
public class CacheCandlesIterator implements ICandlesIterator {

    private ICandlesIterator iterator;

    public CacheCandlesIterator(ICandlesIterator iterator) {
        this.iterator = iterator;
    }

    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {

        List<Candle> candles = CandlesCash.getCandles(security);

        Candle lastCandle = candles.get(candles.size() - 1);

        if (lastCandle.isRelevant())
            return candles;

        candles = iterator.getNextCandlesFor(security, dateFrom, dateTo);

        CandlesCash.addCandles(security, candles);

        return candles;
    }
}
