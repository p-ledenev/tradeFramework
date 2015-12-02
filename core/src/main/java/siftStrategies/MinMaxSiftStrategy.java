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
    @Getter
    private int fillingGapsNumber;


    private double minValue;
    private double maxValue;
    private Candle last;

    public MinMaxSiftStrategy(double sieveParam, int fillingGapsNumber) {
        this.sieveParam = sieveParam;
        this.fillingGapsNumber = fillingGapsNumber;

        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;

        last = null;
    }

    public List<Candle> sift(List<Candle> newCandles) {

        List<Candle> sifted = new ArrayList<Candle>();

        for (Candle candle : newCandles) {

            if (candle.hasValueGreaterThan(maxValue)) maxValue = candle.getValue();
            if (candle.hasValueLessThan(minValue)) minValue = candle.getValue();

            if (candle.computeVariance(maxValue) < sieveParam && candle.computeVariance(minValue) < sieveParam)
                continue;

            if (last != null)
                sifted.addAll(extraBetween(last, candle));

            sifted.add(candle);
            last = candle;

            maxValue = candle.getValue();
            minValue = candle.getValue();
        }

        return sifted;
    }

    private List<Candle> extraBetween(Candle last, Candle newCandle) {
        List<Candle> sifted = new ArrayList<Candle>();

        double sign = Math.signum(newCandle.getValue() - last.getValue());
        if (last.computeVariance(newCandle) > fillingGapsNumber * sieveParam) {
            double value = last.getValue() * (1 + sign * sieveParam / 100.);
            while (sign * value < sign * newCandle.getValue()) {
                Candle clone = newCandle.clone();
                clone.setValue(value);
                sifted.add(clone);

                value = value * (1 + sign * sieveParam / 100.);
            }
        }

        return sifted;
    }
}
