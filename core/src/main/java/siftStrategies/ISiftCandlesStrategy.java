package siftStrategies;

import model.*;

import java.util.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public interface ISiftCandlesStrategy {

    List<Candle> sift(List<Candle> newCandles);
}
