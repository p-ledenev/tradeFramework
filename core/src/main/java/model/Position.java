package model;

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

    public static Position begining() {
        return new Position(OrderDirection.none, 0, new DateTime(0));
    }

    public static Position closing(DateTime date) {
        return new Position(OrderDirection.none, 0, date);
    }

    public static Position opening(OrderDirection direction, int volume, DateTime date) {
        return new Position(direction, volume, date);
    }

    private Position(OrderDirection direction, int volume, DateTime date) {
        this.volume = volume;
        this.direction = direction;
        this.date = new DateTime(date);
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

    public boolean isBegining() {
        return date.equals(new DateTime(0)) && OrderDirection.none.equals(direction);
    }
}
