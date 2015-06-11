package model;

/**
 * Created by DiKey on 04.04.2015.
 */
public class EmptyOrder extends Order {

    public EmptyOrder(Machine machine) {
        super(machine);
    }

    @Override
    public boolean applyToMachine() {
        return false;
    }

    @Override
    public void setValue(double value) {
    }

    @Override
    public double getValue() {
        return 0;
    }

    @Override
    public void print() {
    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public boolean hasOppositeDirection(Order order) {
        return false;
    }
}
