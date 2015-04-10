package model;

import exceptions.PositionAlreadySetFailure;

/**
 * Created by DiKey on 04.04.2015.
 */
public class ExecutableOrder extends Order {

    public ExecutableOrder(Position position, Machine machine) {
        super(position, machine);
    }

    @Override
    public void applyToMachine() throws PositionAlreadySetFailure {
        machine.apply(position);
    }
}
