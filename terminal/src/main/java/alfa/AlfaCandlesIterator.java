package alfa;

import lombok.AllArgsConstructor;
import model.Candle;
import model.ICandlesIterator;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 10.06.2015.
 */

@AllArgsConstructor
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;
    private String market;
    private AlfaTimeframe timeframe;

    public List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
        return gateway.loadMarketData(security, market, timeframe, dateFrom, dateTo);
    }
}
