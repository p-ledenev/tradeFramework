package model;

import lombok.*;
import org.joda.time.*;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Position {

    private Candle candle;
    private Direction direction;
    private int volume;

    public static Position begining() {
        return new Position(Direction.neutral, 0, Candle.empty());
    }

    public static Position closing(Candle candle) {
        return new Position(Direction.neutral, 0, candle);
    }

    public static Position opening(Direction direction, int volume, Candle candle) {
        return new Position(direction, volume, candle);
    }

    private Position(Direction direction, int volume, Candle candle) {
        this.volume = volume;
        this.direction = direction;
        this.candle = candle.clone();
    }

    public double computeProfit(double value) {
        return direction.getSign() * volume * (value - getValue());
    }

    public boolean hasSameDirection(Position position) {
        return direction.equals(position.getDirection());
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
        return Direction.buy.equals(direction);
    }

    public boolean isSell() {
        return Direction.sell.equals(direction);
    }

    public boolean isNeutral() {
        return Direction.neutral.equals(direction);
    }

    public boolean equalTo(Position position) {
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

    public int getSignVolume() {
        return direction.getSign() * volume;
    }
}
