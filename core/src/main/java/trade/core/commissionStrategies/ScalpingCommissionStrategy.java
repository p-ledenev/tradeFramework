package trade.core.commissionStrategies;

import trade.core.model.Position;

/**
 * Created by DiKey on 04.04.2015.
 */
public class ScalpingCommissionStrategy extends ConstantCommissionStrategy {

    public ScalpingCommissionStrategy(double commission) {
        super(commission);
    }

    @Override
    public double computeOpenPositionCommission(Position position) {
        return commission * position.getVolume();
    }

    @Override
    public double computeClosePositionCommission(Position position, Position newPosition) {
        if (position.hasSameDay(newPosition))
            return 0;

        return commission * position.getVolume();
    }
}
