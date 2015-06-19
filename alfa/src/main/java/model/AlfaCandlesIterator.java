package model;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;

    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        List<Candle> candles = gateway.loadMarketData(security, dateFrom, dateTo);

        Log.info("Alfa candle iterator. Loaded candles");
        for (Candle candle : candles)
            Log.info(candle.print());

        return candles;
    }
}
