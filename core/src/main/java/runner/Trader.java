package runner;

import lombok.AllArgsConstructor;
import model.Candle;
import model.CandleProcessor;

/**
 * Created by ledenev.p on 15.05.2015.
 */

@AllArgsConstructor
public class Trader {

    private ICandlesIterator candlesIterator;
    private CandleProcessor candleProcessor;
    private ICollectTradeData dataCollector;

    public void trade() throws Throwable {

        while (candlesIterator.hasNextCandles()) {
            Candle[] candles = candlesIterator.getNextCandles();

            candleProcessor.processNext(candles);
            dataCollector.collect();

            IResultWriter writer = dataCollector.getResultWriter();
            writer.writeNewData();
        }

        IResultWriter writer = dataCollector.getResultWriter();
        writer.writeAllData();
    }
}
