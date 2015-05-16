package siftStrategies;

import model.Candle;

import java.util.List;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public interface ISiftCandlesStrategy {

    List<Candle> sift(Candle base, List<Candle> newCandles);
}
