package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ledenev.p on 04.06.2015.
 */
public class CandlesCash {

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
}
