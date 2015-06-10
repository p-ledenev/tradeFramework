package model;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Position {

    private Candle candle;
    private OrderDirection direction;
    private int volume;

    public static Position begining() {
        return new Position(OrderDirection.none, 0, Candle.empty());
    }

    public static Position closing(Candle candle) {
        return new Position(OrderDirection.none, 0, candle);
    }

    public static Position opening(OrderDirection direction, int volume, Candle candle) {
        return new Position(direction, volume, candle);
    }

    private Position(OrderDirection direction, int volume, Candle candle) {
        this.volume = volume;
        this.direction = direction;
        this.candle = candle.clone();
    }

    public double computeProfit(double value) {
        return direction.getSign() * volume * (value - getValue());
    }

    public boolean hasSameDirection(Position position) {
        return direction.isEqual(position.getDirection());
    }

    public boolean hasSameDay(Position position) {
        return candle.getDate().toLocalDate().equals(position.getDate().toLocalDate());
    }

    public DateTime getDate() {
        return candle.getDate();
    }

    public double getValue() {
        return candle.getValue();
    }

    public int getYear() {
        return getDate().getYear();
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

        return direction.equals(position.getDirection()) && volume == position.getVolume() && getValue() == position.getValue();
    }

    public void setValue(double value) {
        candle.setValue(value);
    }

    public void setDate(DateTime date) {
        candle.setDate(date);
    }

    public String printCSV() {
        return candle.printTitleCSV();
    }
}
