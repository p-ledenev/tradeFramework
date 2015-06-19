package iterators;

import lombok.AllArgsConstructor;
import model.*;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesIterator implements IPortfolioCandlesIterator {

    private ICandlesIterator iterator;

    public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        return null;
    }
}
