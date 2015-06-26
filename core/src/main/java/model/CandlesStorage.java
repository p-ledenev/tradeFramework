package model;

import lombok.*;
import siftStrategies.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */
@Data
public class CandlesStorage {

    private List<Candle> candles;
    private ISiftCandlesStrategy siftStrategy;

    public CandlesStorage() {
    }

    public CandlesStorage(ISiftCandlesStrategy siftStrategy, List<Candle> candles) {
        this.siftStrategy = siftStrategy;
        this.candles = candles;
    }

    public CandlesStorage(ISiftCandlesStrategy siftStrategy) {
        this(siftStrategy, new ArrayList<Candle>());
    }

    public void add(List<Candle> newCandles) {
        List<Candle> sifted = siftStrategy.sift(newCandles);

        for (Candle candle : sifted)
            Log.debug("Added to candle storage: " + candle.print());

        candles.addAll(sifted);
    }

    public int size() {
        return candles.size();
    }

    public boolean lessThan(int size) {
        return size() < size;
    }

    public Candle last() {

        if (candles.size() == 0)
            return Candle.empty();

        return candles.get(candles.size() - 1);
    }

    public Candle get(int index) {
        return candles.get(index);
    }

    public int computeStorageSizeFor(List<Candle> candles) {
        return siftStrategy.sift(candles).size();
    }

    public boolean validateTimeSequence(List<Candle> newCandles) {
        if (newCandles.size() == 0)
            return true;

        return last().before(newCandles.get(0));
    }
}
