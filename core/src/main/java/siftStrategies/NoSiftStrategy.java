package siftStrategies;

import model.Candle;

import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */
public class NoSiftStrategy implements ISiftCandlesStrategy {

    public List<Candle> sift(List<Candle> newCandles) {
        return newCandles;
    }
}
