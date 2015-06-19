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

        if (CandlesCache.hasRelevantDataFor(security))
            return CandlesCache.getCandles(security);

        List<Candle> candles = iterator.getNextCandlesFor(security, dateFrom, dateTo);
        CandlesCache.addCandles(security, candles);

        return candles;
    }

    public List<Candle> getNextCandlesFor(String security, DateTime dateTo, int count) throws Throwable {

        if (CandlesCache.hasRelevantDataFor(security))
            return CandlesCache.getCandles(security);

        List<Candle> candles = iterator.getNextCandlesFor(security, dateTo, count);
        CandlesCache.addCandles(security, candles);

        return candles;
    }
}
