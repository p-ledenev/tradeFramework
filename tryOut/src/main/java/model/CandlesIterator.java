package model;

import runner.ICandlesIterator;

import java.util.List;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public class CandlesIterator implements ICandlesIterator {

    private List<TryOutCandle> candles;
    private int currentIndex;

    public CandlesIterator(List<TryOutCandle> candles) {
        this.candles = candles;
        currentIndex = 0;
    }

    public Candle[] getNextCandles() {
        return new Candle[]{candles.get(currentIndex++)};
    }

    public boolean hasNextCandles() {
        return currentIndex < candles.size();
    }
}
