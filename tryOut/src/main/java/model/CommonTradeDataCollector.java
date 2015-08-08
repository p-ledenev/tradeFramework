package model;

import lombok.*;
import resultWriters.*;

import java.util.*;

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
    protected void collectMachinesTradeData(List<Order> orders) {
        for (MachineMoneyStatesCollector statesCollector : machineCollectors) {

            if (statesCollector.isEmpty() && !orders.isEmpty())
                statesCollector.setInitialMoneyAmount(computeInitialAmount(orders.get(0)));

            statesCollector.addStateIfChanged();
        }
    }
}
