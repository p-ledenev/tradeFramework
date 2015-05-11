package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Trader {

    private List<Candle> candles;
    private CandleProcessor candleProcessor;
    @Getter
    private List<MachineStatesCollector> machineCollectors;
    @Getter
    private PortfolioStateCollector portfolioCollector;

    public Trader(Portfolio portfolio, IOrdersExecutor orderExecutor, List<Candle> candles) {

        candleProcessor = new CandleProcessor(portfolio, orderExecutor);
        portfolioCollector = new PortfolioStateCollector(portfolio);

        machineCollectors = new ArrayList<MachineStatesCollector>();
        for (Machine machine : portfolio.getMachines())
            machineCollectors.add(new MachineStatesCollector(machine));

        this.candles = candles;
    }

    public void trade() throws Throwable {

        for (Candle candle : candles) {
            candleProcessor.processNext(candle);

            for (MachineStatesCollector statesCollector : machineCollectors)
                statesCollector.addStateIfChanged();

            portfolioCollector.addStateIfChanged();
        }
    }
}
