package iterators;

import model.Candle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ledenev.p on 04.06.2015.
 */
public class CandlesCache {

    public static Map<String, List<Candle>> candlesMap;

    static {
        candlesMap = new HashMap<String, List<Candle>>();
    }

    public static void addCandles(String security, List<Candle> candles) {
        candlesMap.put(security, candles);
    }

    public static List<Candle> getCandles(String security) {
        List<Candle> candles = candlesMap.get(security);

        if (candles == null)
            return new ArrayList<Candle>();

        return candles;
    }

    public static boolean hasRelevantDataFor(String security) {
        List<Candle> candles = getCandles(security);

        if (candles.size() <= 0)
            return false;

        Candle lastCandle = candles.get(candles.size()-1);

        return lastCandle.isRelevant(1);
    }
}
