package model;

import model.Candle;
import runner.ICandlesIterator;

import java.util.List;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public class CandlesIterator implements ICandlesIterator {

    private List<Candle> candles;
    private int currentIndex;

    public CandlesIterator(List<Candle> candles) {
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
