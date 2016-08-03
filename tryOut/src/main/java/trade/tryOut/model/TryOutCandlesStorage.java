package trade.tryOut.model;

import trade.core.model.*;
import trade.core.siftStrategies.ISiftCandlesStrategy;

import java.util.List;

/**
 * Created by DiKey on 07.08.2015.
 */
public class TryOutCandlesStorage extends CandlesStorage {

    public TryOutCandlesStorage(ISiftCandlesStrategy siftStrategy) {
        super(siftStrategy);
    }

    @Override
    public boolean add(List<Candle> newCandles) {

        List<Candle> sifted = siftStrategy.sift(newCandles);

        int index = candles.size();
        for (Candle candle : sifted)
            ((TryOutCandle) candle).setIndex(index++);

        candles.addAll(sifted);

        return sifted.size() > 0;
    }
}
