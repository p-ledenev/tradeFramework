package trade.core.takeProfitStrategies;

/**
 * Created by DiKey on 16.05.2015.
 */
public class TakeProfitStrategyFactory {

    public static ITakeProfitStrategy createTakeProfitStrategy() {
        return new NoTakeProfitStrategy();
    }
}
