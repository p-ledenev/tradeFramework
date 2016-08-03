package trade.core.takeProfitStrategies;

import trade.core.model.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public interface ITakeProfitStrategy {

    boolean shouldTakeProfit(CandlesStorage storage, Position currentPosition);
}
