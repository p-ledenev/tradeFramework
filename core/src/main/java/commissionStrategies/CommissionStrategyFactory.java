package commissionStrategies;

/**
 * Created by ledenev.p on 09.11.2015.
 */
public class CommissionStrategyFactory {

    public static ICommissionStrategy createCommissionStrategy(double commission) {
        return new ScalpingCommissionStrategy(commission);
    }
}
