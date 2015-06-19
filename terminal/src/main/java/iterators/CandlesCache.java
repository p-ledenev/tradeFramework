package iterators;

import model.*;
import org.joda.time.*;

import java.util.*;

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

    public static boolean hasRelevantDataFor(String security, DateTime dateFrom, DateTime dateTo) {
        List<Candle> candles = getCandles(security);

        if (candles.size() <= 0)
            return false;

        Candle first = candles.get(0);
        Candle last = candles.get(candles.size() - 1);

        dateFrom = new DateTime(dateFrom.getYear(), dateFrom.getMonthOfYear(), dateFrom.getDayOfMonth(), dateFrom.getHourOfDay(), dateFrom.getMinuteOfHour() + 1);
        dateTo = new DateTime(dateTo.getYear(), dateTo.getMonthOfYear(), dateTo.getDayOfMonth(), dateTo.getHourOfDay(), dateTo.getMinuteOfHour());

        return first.isBeforeOrEqual(dateFrom) && last.isAfterOrEqual(dateTo);
    }
}
