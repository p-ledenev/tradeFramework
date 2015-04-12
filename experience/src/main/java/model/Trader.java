package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Trader {

    protected List<Candle> candles;
    protected CandleProcessor candleProcessor;
    protected List<MachineStatesCollector> collectors;

    public Trader(Portfolio portfolio, IOrdersExecutor orderExecutor, List<Candle> candles) {

        candleProcessor = new CandleProcessor(portfolio, orderExecutor);
        collectors = new ArrayList<MachineStatesCollector>();
        for (Machine machine : portfolio.getMachines())
            collectors.add(new MachineStatesCollector(machine));

        this.candles = candles;
    }

    public void trade() throws Throwable {

        for (Candle candle : candles) {
            candleProcessor.processNext(candle);

            for(MachineStatesCollector statesCollector : collectors)
                statesCollector.addStateIfChanged();
        }
    }
}
