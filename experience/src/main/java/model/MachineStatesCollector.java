package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */

public class MachineStatesCollector extends StatesCollector<Machine> {

    public MachineStatesCollector(Machine machine) {
        super(machine);
    }

    @Override
    protected String getTitle() {
        return Integer.toString(entity.getDepth());
    }

    public Portfolio getPortfolio() {
        return entity.getPortfolio();
    }

    public int getDepth() {
        return entity.getDepth();
    }
}
