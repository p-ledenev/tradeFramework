package iterators;

import model.Candle;
import model.Portfolio;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

public interface IPortfolioCandlesIterator {

    List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable;
}
