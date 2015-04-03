package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class OrderProcessor {

    protected List<Portfolio> portfolios;
    protected ITerminalExecutor executor;

    protected OrderProcessor(List<Portfolio> portfolios, ITerminalExecutor executor) {
        this.portfolios = portfolios;
        this.executor = executor;
    }

    protected OrderProcessor(Portfolio portfolio, ITerminalExecutor executor) {
       this(new ArrayList<Portfolio>(), executor);

        portfolios.add(portfolio);
    }

    protected List<Order> processNext(Candle... candles) throws Throwable {

        List<Order> orders = new ArrayList<Order>();
        for (Portfolio portfolio : portfolios)
            orders.add(portfolio.processCandles(Arrays.asList(candles)));

        executor.execute(orders);

        for (Order order : orders)
            order.applyToMachine();

        return orders;
    }

    public Portfolio getPortfolio(int index) {
        return portfolios.get(index);
    }
}
