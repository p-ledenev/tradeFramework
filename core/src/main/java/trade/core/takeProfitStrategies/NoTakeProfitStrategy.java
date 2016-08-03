package trade.core.takeProfitStrategies;

import trade.core.model.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public class NoTakeProfitStrategy implements ITakeProfitStrategy {

    @Override
    public boolean shouldTakeProfit(CandlesStorage storage, Position currentPosition) {
        return false;
    }
}
