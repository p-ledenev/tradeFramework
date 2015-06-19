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

        if (CandlesCache.hasRelevantDataFor(security, dateFrom, dateTo))
            return CandlesCache.getCandles(security);

        List<Candle> candles = iterator.getNextCandlesFor(security, dateFrom, dateTo);
        CandlesCache.addCandles(security, candles);

        return candles;
    }
}
