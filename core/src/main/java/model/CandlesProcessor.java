package model;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@AllArgsConstructor
public class CandlesProcessor implements ICandlesProcessor {

    protected Portfolio portfolio;
    protected IOrdersExecutor orderExecutor;

    public List<Order> processNext(Candle... candles) throws Throwable {

        List<Order> orders = new ArrayList<Order>();
        orders.addAll(portfolio.processCandles(Arrays.asList(candles)));

        orderExecutor.execute(orders);

        for (Order order : orders)
            order.applyToMachine();

        return orders;
    }
}
