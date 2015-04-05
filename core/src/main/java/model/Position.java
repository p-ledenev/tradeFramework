package model;

import commissionStrategies.ICommissionStrategy;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Position {

    protected DateTime date;
    protected OrderDirection direction;
    protected int volume;
    protected double value;

    protected ICommissionStrategy commissionStrategy;

    public Position(int volume, double value, OrderDirection direction, DateTime date) {
        this.volume = volume;
        this.value = value;
        this.direction = direction;
        this.date = date;
    }

    public double computeAmount() {
        return direction.sign() * value * volume;
    }

    public void increase(Position position) {
        value = (value * volume + position.getVolume() * position.getValue()) / (volume + position.getVolume());
        volume += position.getVolume();
        date = position.getDate();
    }

    public void decrease(Position position) {
        volume -= position.getVolume();
    }

    public void change(Position position) {
        date = position.getDate();
        value = position.getValue();
        volume = position.getVolume() - volume;
    }

    public boolean hasSameDirection(Position position) {
        return direction.isEqual(position.getDirection());
    }

    public boolean hasSameDay(Position position) {
        return date.toLocalDate().equals(position.getDate().toLocalDate());
    }

    public boolean hasNotLessVolumeThan(Position position) {
        return volume >= position.getVolume();
    }

    public double computeIncreasePositionCommission(Position position) {
        return commissionStrategy.computeOpenPositionCommission(position.getValue(), position.getVolume(), hasSameDay(position));
    }

    public double computeDecreasePositionCommission(Position position) {
        return commissionStrategy.computeClosePositionCommission(position.getValue(), position.getVolume(), hasSameDay(position));
    }

    public double computeChangePositionCommission(Position position) {
        return commissionStrategy.computeOpenPositionCommission(position.getValue(), position.getVolume() - volume, hasSameDay(position));
    }
}
