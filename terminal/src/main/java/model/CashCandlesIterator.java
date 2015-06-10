package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */
public class CashCandlesIterator implements ICandlesIterator {

    private ICandlesIterator iterator;

    public CashCandlesIterator(ICandlesIterator iterator) {
        this.iterator = iterator;
    }

    public List<Candle> getNextCandlesFor(String security) throws Throwable {

        List<Candle> candles = CandlesCash.getCandles(security);

        Candle lastCandle = candles.get(candles.size() - 1);

        if (lastCandle.isRelevant())
            return candles;

        candles = iterator.getNextCandlesFor(security);

        CandlesCash.addCandles(security, candles);

        return candles;
    }

    public boolean hasNextCandles() {
        return true;
    }
}
