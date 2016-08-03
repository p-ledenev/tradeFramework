package trade.terminals.core.run;

import trade.core.model.*;
import trade.core.terminals.ITerminalGatewaysFactory;
import trade.terminals.core.iterators.CacheCandlesIterator;
import trade.terminals.core.settings.PortfoliosInitializer;
import trade.terminals.quik.protocols.QuikGatewaysFactory;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

	//public static String dataPath = "d:/Projects/Alfa/java/v1.0/tradeFramework/terminal/data/";
	public static String dataPath = "./";

	public static void main(String[] args) throws Throwable {

		ITerminalGatewaysFactory gatewaysFactory = new QuikGatewaysFactory();

		PortfoliosInitializer initializer = new PortfoliosInitializer();

		ICandlesIterator candlesIterator = new CacheCandlesIterator(gatewaysFactory.getCandleIterator());
		IOrdersExecutor ordersExecutor = gatewaysFactory.getOrderExecutor();

		Trader trader = new Trader(candlesIterator, ordersExecutor, initializer);
		trader.trade();
	}
}
