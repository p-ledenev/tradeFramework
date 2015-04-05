package commissionStrategies;

/**
 * Created by DiKey on 04.04.2015.
 */
public interface ICommissionStrategy {

    public abstract double computeOpenPositionCommission(double value, int volume, boolean intraday);

    public abstract double computeClosePositionCommission(double value, int volume, boolean intraday);
}
