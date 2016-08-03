package trade.tryOut.model;

import trade.core.model.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */

public class MachineMoneyStatesCollector extends MoneyStatesCollector<Machine> {

    public MachineMoneyStatesCollector(Machine machine) {
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
