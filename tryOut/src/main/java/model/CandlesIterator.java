package model;

import java.util.*;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public class CandlesIterator {

    private List<TryOutCandle> candles;
    private int currentIndex;

    public CandlesIterator(List<TryOutCandle> candles) {
        this.candles = candles;
        currentIndex = 0;
    }

    public List<Candle> getNextCandles() {
        List<Candle> nextCandles = new ArrayList<Candle>();
        nextCandles.add(candles.get(currentIndex++));

        return nextCandles;
    }

    public boolean hasNextCandles() {
        return currentIndex < candles.size();
    }
}
