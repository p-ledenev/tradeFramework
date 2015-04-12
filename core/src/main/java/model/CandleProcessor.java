package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class CandleProcessor {

    protected List<Portfolio> portfolios;
    protected IOrdersExecutor orderExecutor;

    protected CandleProcessor(List<Portfolio> portfolios, IOrdersExecutor orderExecutor) {
        this.portfolios = portfolios;
        this.orderExecutor = orderExecutor;
    }

    protected CandleProcessor(Portfolio portfolio, IOrdersExecutor orderExecutor) {
       this(new ArrayList<Portfolio>(), orderExecutor);

        portfolios.add(portfolio);
    }

    protected List<Order> processNext(Candle... candles) throws Throwable {

        List<Order> orders = new ArrayList<Order>();
        for (Portfolio portfolio : portfolios)
            orders.addAll(portfolio.processCandles(Arrays.asList(candles)));

        orderExecutor.execute(orders);

        for (Order order : orders)
            order.applyToMachine();

        return orders;
    }

    public Portfolio getPortfolio(int index) {
        return portfolios.get(index);
    }
}
