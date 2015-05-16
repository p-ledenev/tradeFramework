package model;

import resultWriters.PortfolioDataWriter;
import resultWriters.ResultWriter;
import resultWriters.TradeDataWriter;
import runner.ITradeDataWriter;
import runner.ITradeDataCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 12.05.2015.
 */
public abstract class TradeDataCollector implements ITradeDataCollector {

    PortfolioMoneyStatesCollector portfolioCollector;

    public TradeDataCollector(Portfolio portfolio) {
        portfolioCollector = new PortfolioMoneyStatesCollector(portfolio);
    }

    public void collect() {
        portfolioCollector.addStateIfChanged();
        collectMachinesTradeData();
    }

    protected abstract List<ResultWriter> collectResultWriters();

    protected abstract void collectMachinesTradeData();

    public ITradeDataWriter getResultWriter() {
        List<ResultWriter> resultWriters = new ArrayList<ResultWriter>();

        resultWriters.add(new PortfolioDataWriter(portfolioCollector, "averageMoney"));
        resultWriters.addAll(collectResultWriters());

        return new TradeDataWriter(resultWriters);
    }
}
