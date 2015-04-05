package model;

/**
 * Created by DiKey on 04.04.2015.
 */
public class ExecutableOrder extends Order {

    @Override
    public void applyToMachine() {
        machine.add(position);
    }
}
