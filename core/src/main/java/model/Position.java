package model;

import lombok.*;
import org.joda.time.*;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Position implements Cloneable {

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

    public static Position opening(Direction direction) {
        int volume = direction.isActive() ? 1 : 0;

        return opening(direction, volume, Candle.empty());
    }

    private Position(Direction direction, int volume, Candle candle) {
        this.volume = volume;
        this.direction = direction;
        this.candle = candle.clone();
    }

    public double computeProfit(double value) {
        return direction.getSign() * volume * (value - getValue());
    }

    public boolean hasSameDirectionAs(Position position) {
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

    public boolean isHold() {
        return Direction.hold.equals(direction);
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

    public int getSignVolume() {
        return direction.getSign() * volume;
    }

    public String printCSV() {
        return candle.printTitleCSV();
    }

    public void neutral() {
        direction = Direction.neutral;
    }

    public Position copy() throws CloneNotSupportedException {
        return (Position) this.clone();
    }
}
