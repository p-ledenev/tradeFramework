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

    public double computeMeanDeviation(int year) {
        double mean = 0;

        for (int i = 1; i < candles.size(); i++) {
            Candle c1 = candles.get(i);
            Candle c2 = candles.get(i - 1);

            if (!c1.hasYearAs(year))
                continue;

            mean += Math.abs(c1.getValue() - c2.getValue()) / c1.getValue();
        }

        return mean / candles.size();
    }

    public double computeSigma(int year) {

        double mean = computeMean(year);
        double mean2 = 0;

        for (Candle candle : candles)
            mean2 += Math.pow(candle.getValue(), 2);
        mean2 /= candles.size();

        return Math.pow(mean2 - Math.pow(mean, 2), 0.5) / mean;
    }

    public double computeMean(int year) {

        double mean = 0;
        for (Candle candle : candles) {

            if (!candle.hasYearAs(year))
                continue;

            mean += candle.getValue();
        }

        return mean / candles.size();
    }

    public boolean hasNextCandles() {
        return currentIndex < candles.size();
    }

    public int size() {
        return candles.size();
    }
}
