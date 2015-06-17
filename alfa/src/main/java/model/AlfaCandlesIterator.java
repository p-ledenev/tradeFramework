package model;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;

    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        return gateway.loadMarketData(security, dateFrom, dateTo);
    }
}
