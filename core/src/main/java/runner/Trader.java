package runner;

import lombok.AllArgsConstructor;
import model.Candle;
import model.ICandlesProcessor;
import model.Order;
import tools.Log;

import java.util.List;

/**
 * Created by ledenev.p on 15.05.2015.
 */

@AllArgsConstructor
public class Trader {

    private ICandlesIterator candlesIterator;
    private ICandlesProcessor candlesProcessor;
    private ITradeDataCollector dataCollector;

    public void trade() throws Throwable {

        Candle lastCandle = null;
        while (candlesIterator.hasNextCandles()) {
            Candle[] candles = candlesIterator.getNextCandles();

            List<Order> orders = candlesProcessor.processNext(candles);
            dataCollector.collect(orders);

            ITradeDataWriter writer = dataCollector.getResultWriter();
            writer.writeNewData();

            if (lastCandle == null || !lastCandle.hasSameDay(candles[0])) {
                Log.info("Processing candle - " + candles[0].print());
                lastCandle = candles[0];
            }
        }

        ITradeDataWriter writer = dataCollector.getResultWriter();
        writer.writeAllData();
    }
}
