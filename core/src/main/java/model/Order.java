package model;

import exceptions.PositionAlreadySetFailure;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
public abstract class Order {

    protected Machine machine;
    protected Position position;
    protected boolean isExecuted;


    public Order(Position position, Machine machine) {
        this.machine = machine;
        this.position = position;
    }

    public abstract void applyToMachine() throws PositionAlreadySetFailure;

    public void executed() {
        isExecuted = true;
    }

    public Candle getLastCandle() {
        return machine.getLastCandle();
    }

    public void setPositionValue(double value) {
        position.setValue(value);
    }

    public void setPositionDate(DateTime date) {
        position.setDate(date);
    }

    public double getPositionValue() {
        return position.getValue();
    }
}