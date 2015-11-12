package takeProfitStrategies;

import model.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public interface ITakeProfitStrategy {

    boolean shouldTakeProfit(CandlesStorage storage, Position currentPosition);
}
