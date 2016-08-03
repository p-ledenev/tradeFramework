package trade.terminals.alfa.model;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import trade.core.model.*;
import trade.core.tools.*;

import java.util.List;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;

    public List<Candle> getCandlesInclusiveFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        Log.info("Alfa candle iterator " + security + ". Loading trade.terminals.quik.candles for " + Format.asString(dateFrom) + " - " + Format.asString(dateTo));

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
