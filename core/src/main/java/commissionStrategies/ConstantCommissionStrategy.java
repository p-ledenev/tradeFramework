package commissionStrategies;

/**
 * Created by DiKey on 04.04.2015.
 */
public abstract class ConstantCommissionStrategy implements ICommissionStrategy {

    protected double commission;

    public ConstantCommissionStrategy(double commission) {
        this.commission = commission;
    }
}
