package run;

import lombok.*;
import model.*;
import resultWriters.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 15.05.2015.
 */

@AllArgsConstructor
public class Trader implements Runnable {
    private CandlesIterator candlesIterator;
    private TradeDataCollector dataCollector;
    private IOrdersExecutor orderExecutor;
    private Portfolio portfolio;

    public void trade() throws Throwable {

        Candle lastCandle = null;
        while (candlesIterator.hasNextCandles()) {
            List<Candle> candles = candlesIterator.getNextCandles();

            List<Order> orders = new ArrayList<Order>();
            portfolio.addOrderTo(orders, candles);

            orderExecutor.execute(orders);

            for (Order order : orders)
                order.applyToMachine();

            dataCollector.collect(orders);

            if (lastCandle == null || !lastCandle.hasSameDay(candles.get(0))) {
                Log.info("Portfolio: " + portfolio.getTitle() + "; processing candle - " + candles.get(0).print());
                lastCandle = candles.get(0);
            }
        }

        TradeDataWriter writer = dataCollector.getResultWriter();
        writer.writeData();
    }

    public void run() {
        try {
            trade();
        } catch (Throwable e) {
            Log.error("", e);
        }
    }
}
