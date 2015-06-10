package model;

import exceptions.PositionAlreadySetFailure;
import tools.Log;

/**
 * Created by DiKey on 04.04.2015.
 */
public class ExecutableOrder extends Order {

    private Position position;

    public ExecutableOrder(Position position, Machine machine) {
        super(machine);
        this.position = position;
    }

    @Override
    public void setValue(double value) {
        position.setValue(value);
    }

    @Override
    public double getValue() {
        return position.getValue();
    }

    @Override
    public void print() {
        Log.info(machine.getPortfolioTitle() + " " + machine.getDepth() + " " + position.getDirection() + " " + position.getValue());
    }

    @Override
    public int getVolume() {
        return machine.getPositionVolume() + position.getVolume();
    }

    @Override
    public boolean applyToMachine() throws PositionAlreadySetFailure {

        if (isExecuted)
            machine.apply(position);

        return isExecuted;
    }
}
