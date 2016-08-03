package trade.core.siftStrategies;

/**
 * Created by DiKey on 16.05.2015.
 */
public class SiftCandlesStrategyFactory {

    public static ISiftCandlesStrategy createSiftStrategy(double sieveParam) {
        return createSiftStrategy(sieveParam, ISiftCandlesStrategy.noGapsFilling);
    }

    public static ISiftCandlesStrategy createSiftStrategy(double sieveParam, int fillingGapsNumber) {
        return new MinMaxSiftStrategy(sieveParam, fillingGapsNumber);
        //return new NoSiftStrategy();
    }
}
