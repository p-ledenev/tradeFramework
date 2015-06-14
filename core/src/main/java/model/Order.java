package model;

import exceptions.PositionAlreadySetFailure;
import lombok.Data;
import tools.Log;

/**
 * Created by DiKey on 04.04.2015.
 */

@Data
public class Order {

    protected Position newPosition;
    protected Machine machine;
    protected boolean isExecuted;

    public Order(Machine machine) {
        this.machine = machine;
    }

    public Order(Position newPosition, Machine machine) {
        this(machine);
        this.newPosition = newPosition;
    }

    public void executed() {
        isExecuted = true;
    }

    public Candle getLastCandle() {
        return machine.getLastCandle();
    }

    public String getPortfolioTitle() {
        return machine.getPortfolio().getTitle();
    }

    public boolean hasSameSecurity(Order order) {
        return getSecurity().equals(order.getSecurity());
    }

    public String getSecurity() {
        return machine.getSecurity();
    }

    public void setValue(double value) {
        newPosition.setValue(value);
    }

    public double getValue() {
        return newPosition.getValue();
    }

    public void print() {
        Log.info(toString());
    }

    public int getVolume() {
        return machine.getPositionVolume() + newPosition.getVolume();
    }

    public OrderDirection getDirection() {
        return machine.getPositionDirection().opposite();
    }

    public boolean hasOppositeDirection(Order order) {
        return false;
    }

    public boolean applyToMachine() throws PositionAlreadySetFailure {

        if (isExecuted)
            machine.apply(newPosition);

        return isExecuted;
    }

    @Override
    public String toString() {
        return machine.getPortfolioTitle() + " " + machine.getDepth() + " " +
                newPosition.getDirection() + " " + getValue() + " " + getVolume();
    }
}
