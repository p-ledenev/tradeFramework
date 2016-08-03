package trade.core.terminals;

import trade.core.model.*;

/**
 * Created by ledenev.p on 22.03.2016.
 */
public interface ITerminalGatewaysFactory {

	ICandlesIterator getCandleIterator() throws Throwable;

	IOrdersExecutor getOrderExecutor() throws Throwable;
}
