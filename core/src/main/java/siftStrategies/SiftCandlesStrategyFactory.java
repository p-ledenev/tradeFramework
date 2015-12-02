package siftStrategies;

/**
 * Created by DiKey on 16.05.2015.
 */
public class SiftCandlesStrategyFactory {

    private static int noGapsFilling = 2000;

    public static ISiftCandlesStrategy createSiftStrategy(double sieveParam) {
        return createSiftStrategy(sieveParam, noGapsFilling);
    }

    public static ISiftCandlesStrategy createSiftStrategy(double sieveParam, int fillingGapsNumber) {
        return new MinMaxSiftStrategy(sieveParam, fillingGapsNumber);
        //return new NoSiftStrategy();
    }
}
