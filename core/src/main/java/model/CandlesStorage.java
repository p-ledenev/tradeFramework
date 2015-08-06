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
        candles = new ArrayList<Candle>();
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

    public List<Candle> getAfter(Candle candle, int depth) {

        int index = (candles.size() - 1) / 2;
        int section = candles.size();
        while (!candle.hasSameDay(candles.get(index))) {

            int newIndex = 0;
            section /= 2;
            if (candle.before(candles.get(index))) {
                newIndex = index - section / 2;
                index = newIndex == index ? newIndex - 1 : newIndex;

            } else {
                newIndex = index + section / 2;
                index = newIndex == index ? newIndex + 1 : newIndex;
            }

            if (index < 0 || index >= candles.size())
                return new ArrayList<Candle>();
        }

        List<Candle> response = new ArrayList<Candle>();
        int length = candles.size() > index + depth ? index + depth : candles.size();
        for (int i = index + 1; i < length + 1; i++)
            response.add(candles.get(i));

        return response;
    }


}
