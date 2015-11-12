package siftStrategies;

/**
 * Created by DiKey on 16.05.2015.
 */
public class SiftCandlesStrategyFactory {

    public static ISiftCandlesStrategy createSiftStrategy(double sieveParam) {
        return new MinMaxSiftStrategy(sieveParam);
        //return new NoSiftStrategy();
    }
}
