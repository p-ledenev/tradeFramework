package trade.core.siftStrategies;

import trade.core.model.Candle;

import java.util.*;

/**
 * Created by DiKey on 12.04.2015.
 */

public class SimpleSiftStrategy implements ISiftCandlesStrategy {

    private Double sieveParam;
    private Double lastValue;

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

    @Override
    public Integer getFillingGapsNumber() {
        return noGapsFilling;
    }

    @Override
    public Double getSieveParam() {
        return sieveParam;
    }
}
