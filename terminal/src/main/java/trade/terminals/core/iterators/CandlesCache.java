package trade.terminals.core.iterators;

import org.joda.time.DateTime;
import trade.core.model.Candle;

import java.util.*;

/**
 * Created by ledenev.p on 04.06.2015.
 */
public class CandlesCache {

    public static Map<String, List<Candle>> candlesMap;

    static {
        candlesMap = new HashMap<>();
    }

    public static void addCandles(String security, List<Candle> candles) {
        candlesMap.put(security, candles);
    }

    public static List<Candle> getCandles(String security) {
        List<Candle> candles = candlesMap.get(security);

        if (candles == null)
            return new ArrayList<>();

        return candles;
    }

    public static boolean hasRelevantDataFor(String security, DateTime dateFrom, DateTime dateTo) {
        List<Candle> candles = getCandles(security);

        if (candles.size() <= 0)
            return false;

        Candle first = candles.get(0);
        Candle last = candles.get(candles.size() - 1);

        return first.hasDate(dateFrom.plusMinutes(1)) && last.hasDate(dateTo);
    }
}
