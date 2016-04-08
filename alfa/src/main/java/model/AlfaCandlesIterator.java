package model;

import lombok.*;
import org.joda.time.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;

    public List<Candle> getCandlesInclusiveFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        Log.info("Alfa candle iterator " + security + ". Loading candles for " + Format.asString(dateFrom) + " - " + Format.asString(dateTo));

        List<Candle> candles = gateway.loadMarketData(security, dateFrom, dateTo);

        validateTimeSequence(candles, dateTo);

        return candles;
    }

    private void validateTimeSequence(List<Candle> candles, DateTime dateTo) {

        if (candles.size() == 0)
            return;

        int last = candles.size() - 1;
        if (candles.get(last).hasDate(dateTo.plusMinutes(1)))
            candles.remove(last);
    }
}
