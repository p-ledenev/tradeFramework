package siftStrategies;

import model.Candle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */
public class SimpleSiftStrategy extends SiftCandlesStrategy {

    public SimpleSiftStrategy(double sieveParam) {
        super(sieveParam);
    }

    @Override
    public List<Candle> sift(Candle base, List<Candle> newCandles) {

        List<Candle> sifted = new ArrayList<Candle>();
        double lastValue = base.getValue();

        for (Candle candle : newCandles) {
            if (candle.computeVariance(lastValue) >= sieveParam) {
                sifted.add(candle);
                lastValue = candle.getValue();
            }
        }

        return sifted;
    }
}
