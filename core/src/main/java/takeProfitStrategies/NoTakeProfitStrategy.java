package takeProfitStrategies;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public class NoTakeProfitStrategy implements ITakeProfitStrategy {

    @Override
    public boolean shouldTakeProfit() {
        return false;
    }
}
