package model;

import commissionStrategies.ICommissionStrategy;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Position {

    private DateTime date;
    private OrderDirection direction;
    private int volume;
    private double value;

    protected ICommissionStrategy commissionStrategy;

    public static Position closing() {
        return new Position(0, OrderDirection.none);
    }

    public static Position opening(OrderDirection direction, int volume) {
        return new Position(volume, direction);
    }

    private Position(int volume, OrderDirection direction) {
        this.volume = volume;
        this.direction = direction;
    }

    public double computeProfit(double value) {
        return direction.getSign() * volume * (value - this.value);
    }

    public boolean hasSameDirection(Position position) {
        return direction.isEqual(position.getDirection());
    }

    public boolean hasSameDay(Position position) {
        return date.toLocalDate().equals(position.getDate().toLocalDate());
    }

    public double computeClosingCommission(Position newPosition) {
        return commissionStrategy.computeClosePositionCommission(newPosition.getValue(), volume, hasSameDay(newPosition));
    }

    public double computeOpeningCommission() {
        return commissionStrategy.computeOpenPositionCommission(value, volume);
    }
}
