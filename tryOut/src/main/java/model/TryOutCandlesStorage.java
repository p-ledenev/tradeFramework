package model;

import siftStrategies.ISiftCandlesStrategy;
import tools.Format;

import java.util.List;

/**
 * Created by DiKey on 07.08.2015.
 */
public class TryOutCandlesStorage extends CandlesStorage {

    public TryOutCandlesStorage(ISiftCandlesStrategy siftStrategy) {
        super(siftStrategy);
    }

    @Override
    public void add(List<Candle> newCandles) {

        List<Candle> sifted = siftStrategy.sift(newCandles);

        int index = candles.size();
        for (Candle candle : sifted)
            ((TryOutCandle) candle).setIndex(index++);

        candles.addAll(sifted);
    }
}
