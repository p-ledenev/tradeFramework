package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */
public interface ITerminalGateway {

    List<Candle> getNextCandlesFor(String security);
}
