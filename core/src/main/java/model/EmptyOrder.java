package model;

/**
 * Created by DiKey on 04.04.2015.
 */
public class EmptyOrder extends Order {

    public EmptyOrder(Machine machine) {
        super(machine);
    }

    @Override
    public void applyToMachine() {
    }
}
