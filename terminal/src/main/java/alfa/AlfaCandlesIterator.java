package alfa;

import model.Candle;

import java.util.Date;
import java.util.List;

/**
 * Created by ledenev.p on 10.06.2015.
 */
public class AlfaCandlesIterator implements ICandlesIterator {

    private AlfaGateway gateway;
    private String market;
    private AlfaTimeframe timeframe;

    public AlfaCandlesIterator(AlfaGateway gateway) {
        this.gateway = gateway;
    }

    public List<Candle> getNextCandlesFor(String security) throws Throwable {

        return gateway.loadMarketData(security, market, timeframe, )
    }

    public boolean hasNextCandles() {
        return false;
    }
}
