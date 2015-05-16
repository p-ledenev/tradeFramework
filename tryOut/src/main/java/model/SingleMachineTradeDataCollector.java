package model;

import resultWriters.DetailMachineDataWriter;
import resultWriters.ResultWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 12.05.2015.
 */
public class SingleMachineTradeDataCollector extends TradeDataCollector {

    private MachinePositionsCollector positionsCollector;
    private StrategyStatesCollector statesCollector;

    public SingleMachineTradeDataCollector(Portfolio portfolio) {
        super(portfolio);

        Machine machine = portfolio.getMachines().get(0);

        positionsCollector = new MachinePositionsCollector(machine);
        statesCollector = new StrategyStatesCollector(machine.getDecisionStrategy());
    }

    @Override
    protected List<ResultWriter> collectResultWriters() {
        return new ArrayList<ResultWriter>() {
            {
                add(new DetailMachineDataWriter(positionsCollector, statesCollector, "tradeResult"));
            }
        };
    }

    @Override
    protected void collectMachinesTradeData() {
        positionsCollector.addPositionIfChanged();
    }
}
