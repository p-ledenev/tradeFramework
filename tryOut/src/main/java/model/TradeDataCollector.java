package model;

import resultWriters.PortfolioDataWriter;
import resultWriters.ResultWriter;
import resultWriters.TradeDataWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 12.05.2015.
 */
public abstract class TradeDataCollector {

    PortfolioMoneyStatesCollector portfolioCollector;

    public TradeDataCollector(Portfolio portfolio) {
        portfolioCollector = new PortfolioMoneyStatesCollector(portfolio);
    }

    public void collect(List<Order> orders) {

        if (!isExecuted(orders))
             return;

        if (portfolioCollector.isEmpty())
            portfolioCollector.setInitialMoneyAmount(computeInitialAmount(orders.get(0)));

        portfolioCollector.addStateIfChanged();

        collectMachinesTradeData(orders);
    }

    protected abstract List<ResultWriter> collectResultWriters();

    protected abstract void collectMachinesTradeData(List<Order> orders);

    public TradeDataWriter getResultWriter() {
        List<ResultWriter> resultWriters = new ArrayList<ResultWriter>();

        resultWriters.add(new PortfolioDataWriter(portfolioCollector, "averageMoney"));
        resultWriters.addAll(collectResultWriters());

        return new TradeDataWriter(resultWriters);
    }

    protected double computeInitialAmount(Order order) {
        return order.getValue() * portfolioCollector.getLot();
    }

    protected boolean isExecuted(List<Order> orders) {
        return orders.get(0).isExecuted();
    }

}
