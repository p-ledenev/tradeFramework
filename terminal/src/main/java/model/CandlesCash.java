package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ledenev.p on 04.06.2015.
 */
public class CandlesCash {

    public static Map<String, Candle> quotes;

    static {
        quotes = new HashMap<String, Candle>();
    }

    public static void addQuote(String security, Candle candle) {
        quotes.put(security, candle);
    }

    public static Candle getQuote(String security) {
        Candle quote = quotes.get(security);

        if (quote == null)
            return Candle.empty();

        return quote;
    }
}
