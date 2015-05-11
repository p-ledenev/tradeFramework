package model.testing;

import commissionStrategies.ICommissionStrategy;

/**
 * Created by DiKey on 11.05.2015.
 */
public class CommissionStrategyStub implements ICommissionStrategy {

    private double commission;

    CommissionStrategyStub(double commission) {
        this.commission = commission;
    }

    @Override
    public double computeOpenPositionCommission(double value, int volume) {
        return commission * volume;
    }

    @Override
    public double computeClosePositionCommission(double value, int volume, boolean intraday) {
        return commission * volume;
    }
}
