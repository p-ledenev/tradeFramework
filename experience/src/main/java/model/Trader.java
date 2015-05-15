package model;

import resultWriters.PortfolioDataWriter;
import resultWriters.ResultWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 12.05.2015.
 */
public abstract class Trader {

    protected List<Candle> candles;
    protected CandleProcessor candleProcessor;
    PortfolioMoneyStatesCollector portfolioCollector;

    public Trader(Portfolio portfolio, IOrdersExecutor orderExecutor, List<Candle> candles) {
        this.candles = candles;
        candleProcessor = new CandleProcessor(portfolio, orderExecutor);

        portfolioCollector = new PortfolioMoneyStatesCollector(portfolio);
    }

    public void trade() throws Throwable {

        for (Candle candle : candles) {
            candleProcessor.processNext(candle);

            portfolioCollector.addStateIfChanged();

            collectMachinesTradeData();
        }
    }

    public List<ResultWriter> getResultsWriters() {
        List<ResultWriter> writers = new ArrayList<ResultWriter>();

        writers.add(new PortfolioDataWriter(portfolioCollector, "averageMoney"));
        writers.addAll(collectResultWriters());

        return writers;
    }

    protected abstract List<ResultWriter> collectResultWriters();

    protected abstract void collectMachinesTradeData();

}
