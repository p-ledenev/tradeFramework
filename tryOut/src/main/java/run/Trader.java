package run;

import lombok.AllArgsConstructor;
import model.*;
import resultWriters.TradeDataWriter;
import tools.Log;

import java.util.List;

/**
 * Created by ledenev.p on 15.05.2015.
 */

@AllArgsConstructor
public class Trader {
    private CandlesIterator candlesIterator;
    private TradeDataCollector dataCollector;
    private IOrdersExecutor orderExecutor;
    private Portfolio portfolio;

    public void trade() throws Throwable {

        Candle lastCandle = null;
        while (candlesIterator.hasNextCandles()) {
            List<Candle> candles = candlesIterator.getNextCandles();
            List<Order> orders = portfolio.processCandles(candles);

            orderExecutor.execute(orders);

            for (Order order : orders)
                order.applyToMachine();

            dataCollector.collect(orders);

            if (lastCandle == null || !lastCandle.hasSameDay(candles.get(0))) {
                Log.info("Portfolio: " + orders.get(0).getPortfolioTitle() + "; processing candle - " + candles.get(0).print());
                lastCandle = candles.get(0);
            }
        }

        TradeDataWriter writer = dataCollector.getResultWriter();
        writer.writeData();
    }
}
