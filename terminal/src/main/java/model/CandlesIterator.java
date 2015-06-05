package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */
public class CandlesIterator {

    private ITerminalGateway gateway;

    public List<Candle> getNextCandlesFor(String security) {

        Quote quote = CandlesCash.getQuote(security);

    }
}
