package siftStrategies;

import model.Candle;

import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */
public class NoSiftStrategy extends SiftCandlesStrategy {

    public NoSiftStrategy() {
        super(0);
    }

    @Override
    public List<Candle> sift(Candle base, List<Candle> newCandles) {
        return newCandles;
    }
}
