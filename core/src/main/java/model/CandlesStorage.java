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

    protected List<Candle> candles;
    protected ISiftCandlesStrategy siftStrategy;

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


    public boolean add(List<Candle> newCandles) {

        List<Candle> sifted = siftStrategy.sift(newCandles);

        if (Log.isDebugEnabled())
            for (Candle candle : sifted)
                Log.debug("Added to candle storage (" + getSieveParam() + ", " +
                        getFillingGapsNumber() + ") " + candle.print());

        candles.addAll(sifted);

        return sifted.size() > 0;
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

    public List<Candle> last(int depth) {

        List<Candle> range = new ArrayList<Candle>();
        int size = candles.size();

        if (size < depth)
            return range;

        for (int i = 0; i < depth; i++)
            range.add(candles.get(size - depth + i));

        return range;
    }

    public int findIndexFor(Candle candle) {

        int index = (candles.size() - 1) / 2;
        int section = candles.size();
        while (!candle.hasSameDate(candles.get(index))) {

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
                return -1;
        }

        return index;
    }

    public List<Candle> getAfter(Candle candle, int depth) {

        int index = findIndexFor(candle);
        if (index < 0)
            return new ArrayList<Candle>();

        List<Candle> response = new ArrayList<Candle>();
        int length = candles.size() > index + depth ? index + depth : candles.size() - 1;
        for (int i = index + 1; i < length + 1; i++)
            response.add(candles.get(i));

        return response;
    }

    public Candle backTo(int depth) {
        return last(depth).get(0);
    }

    public Double getSieveParam() {
        return siftStrategy.getSieveParam();
    }

    public Integer getFillingGapsNumber() {
        return siftStrategy.getFillingGapsNumber();
    }
}
