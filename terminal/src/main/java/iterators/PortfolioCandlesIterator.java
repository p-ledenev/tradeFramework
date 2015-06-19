package iterators;

import lombok.AllArgsConstructor;
import model.*;
import org.joda.time.*;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesIterator implements IPortfolioCandlesIterator {

    private ICandlesIterator iterator;

    public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        Candle candle = portfolio.getLastCandle();

        return iterator.getNextCandlesFor(portfolio.getSecurity(), candle.getDate().plusSeconds(2), DateTime.now());
    }
}
