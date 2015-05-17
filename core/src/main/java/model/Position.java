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

    public static Position closing() {
        return new Position(0, OrderDirection.none);
    }

    public static Position opening(OrderDirection direction, int volume) {
        return new Position(volume, direction);
    }

    private Position(int volume, OrderDirection direction) {
        this.volume = volume;
        this.direction = direction;
        date = new DateTime(0);
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

    public int getYear() {
        return date.getYear();
    }

    public boolean isBuy() {
        return direction.isEqual(OrderDirection.buy);
    }

    public boolean isSell() {
        return direction.isEqual(OrderDirection.sell);
    }

    public boolean isNone() {
        return direction.isEqual(OrderDirection.none);
    }

    public boolean equals(Position position) {
        if (position == null)
            return false;

        return direction.equals(position.getDirection()) && volume == position.getVolume() && value == position.getValue();
    }
}
