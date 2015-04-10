package siftStrategies;

import model.Candle;

import java.util.List;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public abstract class SiftCandlesStrategy {

    protected double sieveParam;

    public SiftCandlesStrategy(double sieveParam) {
        this.sieveParam = sieveParam;
    }

    public abstract List<Candle> sift(Candle base, List<Candle> newCandles);
}
