package commissionStrategies;

/**
 * Created by DiKey on 04.04.2015.
 */
public class ScalpingCommissionStrategy extends ConstantCommissionStrategy {

    public ScalpingCommissionStrategy(double commission) {
        super(commission);
    }

    @Override
    public double computeOpenPositionCommission(double value, int volume, boolean intraday) {
        return commission * volume;
    }

    @Override
    public double computeClosePositionCommission(double value, int volume, boolean intraday) {
        if (intraday)
            return 0;

        return commission * volume;
    }
}
