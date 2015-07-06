package siftStrategies;

import model.Candle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 06.07.2015.
 */
public class MovingSiftCandlesStrategy implements ISiftCandlesStrategy {

    private ISiftCandlesStrategy strategy;
    private int depth;
    private List<Candle> forSifting;

    public MovingSiftCandlesStrategy(ISiftCandlesStrategy strategy, int depth) {
        this.strategy = strategy;
        this.depth = depth;

        forSifting = new ArrayList<Candle>();
    }


    @Override
    public List<Candle> sift(List<Candle> newCandles) {

        forSifting.addAll(newCandles);

        for (int i = 0; i < forSifting.size() - depth; i++)
            forSifting.remove(i);

        if (forSifting.size() >= depth)
            strategy.setSieveParam(computeSieveParam());

        return strategy.sift(newCandles);
    }

    private double computeSieveParam() {
        double mean = 0;

        for (int i = 1; i < forSifting.size(); i++) {
            Candle c1 = forSifting.get(i);
            Candle c2 = forSifting.get(i - 1);

            mean += Math.abs(c1.getValue() - c2.getValue()) / c1.getValue();
        }

        return mean / forSifting.size();
    }

    @Override
    public void setSieveParam(double sieveParam) {
        strategy.setSieveParam(sieveParam);
    }
}
