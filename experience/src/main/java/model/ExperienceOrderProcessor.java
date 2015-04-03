package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class ExperienceOrderProcessor {

    protected List<Candle> candles;
    protected OrderProcessor processor;
    protected List<MachineStatesCollector> collectors;

    public ExperienceOrderProcessor(Portfolio portfolio, ITerminalExecutor executor, List<Candle> candles) {

        processor = new OrderProcessor(portfolio, executor);
        collectors = new ArrayList<MachineStatesCollector>();
        for (Machine machine : portfolio.getMachines())
            collectors.add(new MachineStatesCollector(machine));

        this.candles = candles;
    }

    public void trade() throws Throwable {

        for (Candle candle : candles) {
            processor.processNext(candle);

            for(MachineStatesCollector collector : collectors)
                collector.addStateIfChanged();
        }
    }
}
