package commissionStrategies;

import model.Position;

/**
 * Created by DiKey on 04.04.2015.
 */
public interface ICommissionStrategy {

    double computeOpenPositionCommission(Position position);

    double computeClosePositionCommission(Position position, Position newPosition);
}
