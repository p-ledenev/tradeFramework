package trade.core.model.testing;

import trade.core.commissionStrategies.*;
import trade.core.model.Position;

/**
 * Created by DiKey on 11.05.2015.
 */
public class CommissionStrategyStub implements ICommissionStrategy {

    private double commission;

    CommissionStrategyStub(double commission) {
        this.commission = commission;
    }

    @Override
    public double computeOpenPositionCommission(Position position) {
        return commission*position.getVolume();
    }

    @Override
    public double computeClosePositionCommission(Position position, Position newPosition) {
        return commission*position.getVolume();
    }
}
