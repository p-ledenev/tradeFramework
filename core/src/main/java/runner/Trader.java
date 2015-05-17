package runner;

import lombok.AllArgsConstructor;
import model.Candle;
import model.ICandlesProcessor;
import tools.Log;

/**
 * Created by ledenev.p on 15.05.2015.
 */

@AllArgsConstructor
public class Trader {

    private ICandlesIterator candlesIterator;
    private ICandlesProcessor candlesProcessor;
    private ITradeDataCollector dataCollector;

    public void trade() throws Throwable {

        while (candlesIterator.hasNextCandles()) {
            Candle[] candles = candlesIterator.getNextCandles();

            candlesProcessor.processNext(candles);
            dataCollector.collect();

            ITradeDataWriter writer = dataCollector.getResultWriter();
            writer.writeNewData();

            Log.info("Processing candle " + candles[0].print());
        }

        ITradeDataWriter writer = dataCollector.getResultWriter();
        writer.writeAllData();
    }
}
