package model;

import lombok.Getter;
import resultWriters.MachinesDataWriter;
import resultWriters.ResultWriter;
import resultWriters.SummaryDataWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class CommonTradeDataCollector extends TradeDataCollector {
    @Getter
    private List<MachineMoneyStatesCollector> machineCollectors;

    public CommonTradeDataCollector(Portfolio portfolio) {
        super(portfolio);

        machineCollectors = new ArrayList<MachineMoneyStatesCollector>();
        for (Machine machine : portfolio.getMachines())
            machineCollectors.add(new MachineMoneyStatesCollector(machine));
    }

    @Override
    protected List<ResultWriter> collectResultWriters() {
        return new ArrayList<ResultWriter>() {
            {
                add(new MachinesDataWriter(machineCollectors, "machinesMoney"));
                add(new SummaryDataWriter(machineCollectors, portfolioCollector, "machinesSummary"));
            }
        };
    }

    @Override
    protected void collectMachinesTradeData() {
        for (MachineMoneyStatesCollector statesCollector : machineCollectors)
            statesCollector.addStateIfChanged();
    }
}
