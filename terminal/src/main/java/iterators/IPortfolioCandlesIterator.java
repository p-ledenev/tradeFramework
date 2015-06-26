package iterators;

import model.*;

import java.util.*;

/**
 * Created by ledenev.p on 17.06.2015.
 */

public interface IPortfolioCandlesIterator {

    List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable;
}
