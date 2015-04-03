package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */

public class MachineStatesCollector {

    protected Machine machine;
    @Getter
    protected List<MachineState> states;

    public MachineStatesCollector(Machine machine) {
        this.machine = machine;

        states = new ArrayList<MachineState>();
    }

    public void addStateIfChanged() {

        if (machine.getState().equals(getLastMachineState()))
            return;

        states.add(machine.getState());
    }

    protected MachineState getLastMachineState() {
        if (states.size() == 0)
            return null;

        return states.get(states.size() - 1);
    }

    public Portfolio getPortfolio() {
        return machine.getPortfolio();
    }

    protected int getYear() {
        return getLastMachineState().getDate().getYear();
    }

    public String printState(int index) {
    }
}
