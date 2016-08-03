package trade.terminals.core.iterators;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import trade.core.model.*;
import trade.core.tools.Log;

import java.util.*;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesInitializer implements IPortfolioCandlesIterator {

	private ICandlesIterator iterator;

	public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {

		int requiredSize = portfolio.computeInitialCandlesSize();
		List<Candle> candles = new ArrayList<Candle>();

		int maxDepth = portfolio.getMaxDepth();

		DateTime dateTo = DateTime.now().withSecondOfMinute(0).minusMinutes(1);
		DateTime dateFrom = dateTo.minusMinutes(maxDepth * 5);

		int siftedSize = 0;
		while (siftedSize < requiredSize) {
			try {
				candles = iterator.getCandlesInclusiveFor(portfolio.getSecurity(), dateFrom, dateTo);
				siftedSize = portfolio.computeStorageSizeFor(candles);
				dateFrom = dateFrom.minusMinutes(maxDepth);
			} catch (Throwable e) {
				Log.error(e);
				break;
			}
		}

		return candles;
	}
}
