package siftStrategies;

import lombok.*;
import model.*;

import java.util.*;

/**
 * Created by DiKey on 12.04.2015.
 */

public class MinMaxSiftStrategy implements ISiftCandlesStrategy {

    @Getter
    private double sieveParam;
    private double minValue;
    private double maxValue;

    public MinMaxSiftStrategy(double sieveParam) {
        this.sieveParam = sieveParam;
        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;
    }

    public List<Candle> sift(List<Candle> newCandles) {

        List<Candle> sifted = new ArrayList<Candle>();

        for (Candle candle : newCandles) {

            if (candle.greaterThan(maxValue)) maxValue = candle.getValue();
            if (candle.lessThan(minValue)) minValue = candle.getValue();

            if (candle.computeVariance(maxValue) < sieveParam && candle.computeVariance(minValue) < sieveParam)
                continue;

            sifted.add(candle);

            maxValue = candle.getValue();
            minValue = candle.getValue();
        }

        return sifted;
    }
}
