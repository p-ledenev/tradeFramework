package trade.terminals.core.iterators;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import trade.core.model.*;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesIterator implements IPortfolioCandlesIterator {

	private ICandlesIterator iterator;

	public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {

		DateTime dateFrom = portfolio.getLastCandle().getDate().plusMinutes(1).withSecondOfMinute(0);
		DateTime dateTo = DateTime.now().minusMinutes(1).withSecondOfMinute(0);

		return iterator.getCandlesInclusiveFor(portfolio.getSecurity(), dateFrom, dateTo);
	}
}
