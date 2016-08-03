package trade.terminals.quik.protocols;

import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import trade.core.model.*;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */

@AllArgsConstructor
public class QuikCandlesIterator implements ICandlesIterator {

	private QuikDataGateway gateway;

	@Override
	public List<Candle> getCandlesInclusiveFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {
		return gateway.loadMarketDataWithThrows(security, dateFrom, dateTo);
	}
}
