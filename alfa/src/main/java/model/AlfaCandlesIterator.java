package model;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;

    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        return gateway.loadMarketData(security, dateFrom, dateTo);
    }

    public List<Candle> getNextCandlesFor(String security, DateTime dateTo, int count) throws Throwable {

        List<Candle> candles = new ArrayList<Candle>();

        DateTime dateFrom = dateTo.minusDays(1);
        while (candles.size() < count) {
            dateFrom = dateFrom.minusDays(1);
            candles = gateway.loadMarketData(security, dateFrom, dateTo);
        }

        return candles;
    }
}
