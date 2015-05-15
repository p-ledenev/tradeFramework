package runner;

import model.Candle;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public interface ICandlesIterator {

    Candle[] getNextCandles();

    boolean hasNextCandles();
}
