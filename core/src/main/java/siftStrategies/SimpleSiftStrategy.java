package siftStrategies;

import model.*;

import java.util.*;

/**
 * Created by DiKey on 12.04.2015.
 */

public class SimpleSiftStrategy implements ISiftCandlesStrategy {

    private double sieveParam;
    private double lastValue;

    public SimpleSiftStrategy(double sieveParam) {
        this.sieveParam = sieveParam;
        lastValue = Double.MAX_VALUE;
    }


    public List<Candle> sift(List<Candle> newCandles) {

        List<Candle> sifted = new ArrayList<Candle>();

        for (Candle candle : newCandles) {
            if (candle.computeVariance(lastValue) >= sieveParam) {
                sifted.add(candle);
                lastValue = candle.getValue();
            }
        }

        return sifted;
    }
}
