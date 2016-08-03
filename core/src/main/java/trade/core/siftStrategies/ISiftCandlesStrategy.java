package trade.core.siftStrategies;

import trade.core.model.Candle;

import java.util.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public interface ISiftCandlesStrategy {

    Integer noGapsFilling = 2000;
    Double noSieve = 0.;

    List<Candle> sift(List<Candle> newCandles);

    Integer getFillingGapsNumber();

    Double getSieveParam();
}
