package model;

import exceptions.PositionAlreadySetFailure;
import lombok.Data;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
public abstract class Order {

    protected Machine machine;
    protected boolean isExecuted;

    public Order(Machine machine) {
        this.machine = machine;
    }

    public abstract boolean applyToMachine() throws PositionAlreadySetFailure;

    public void executed() {
        isExecuted = true;
    }

    public Candle getLastCandle() {
        return machine.getLastCandle();
    }

    public String getPortfolioTitle() {
        return machine.getPortfolio().getTitle();
    }

    public abstract void setValue(double value);

    public abstract double getValue();

    public abstract void print();

    public abstract int getVolume();

    public boolean hasSameSecurity(Order order) {
        return getSecurity().equals(order.getSecurity());
    }

    public String getSecurity() {
        return machine.getSecurity();
    }

    public abstract boolean hasOppositeDirection(Order order);
}