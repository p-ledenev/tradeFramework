package siftStrategies;

import model.Candle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */
public class MinMaxSiftStrategy extends SiftCandlesStrategy {

    public MinMaxSiftStrategy(double sieveParam) {
        super(sieveParam);
    }

    @Override
    public List<Candle> sift(Candle base, List<Candle> newCandles) {

        List<Candle> sifted = new ArrayList<Candle>();

        double maxValue = base.getValue();
        double minValue = base.getValue();

        for (Candle candle : newCandles) {

            if (candle.greatThan(maxValue)) maxValue = candle.getValue();
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
