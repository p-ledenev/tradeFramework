package trade.terminals.core.iterators;

import trade.core.model.*;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

public interface IPortfolioCandlesIterator {

	List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable;
}
