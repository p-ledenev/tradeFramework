package siftStrategies;

import model.*;

import java.util.*;

/**
 * Created by DiKey on 12.04.2015.
 */
public class NoSiftStrategy implements ISiftCandlesStrategy {

    public List<Candle> sift(List<Candle> newCandles) {
        return newCandles;
    }

    @Override
    public Integer getFillingGapsNumber() {
        return noGapsFilling;
    }

    @Override
    public Double getSieveParam() {
        return noSieve;
    }
}
