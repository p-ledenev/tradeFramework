package trade.terminals.core.iterators;

import org.joda.time.DateTime;
import trade.core.model.*;

import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */
public class CacheCandlesIterator implements ICandlesIterator {

    private ICandlesIterator iterator;

    public CacheCandlesIterator(ICandlesIterator iterator) {
        this.iterator = iterator;
    }

    public List<Candle> getCandlesInclusiveFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {

        List<Candle> candles;
        if (CandlesCache.hasRelevantDataFor(security, dateFrom, dateTo)) {
            candles = CandlesCache.getCandles(security);
        } else {
            candles = iterator.getCandlesInclusiveFor(security, dateFrom, dateTo);
            CandlesCache.addCandles(security, candles);
        }

        return candles;
    }
}
