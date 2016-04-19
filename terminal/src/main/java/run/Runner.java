package run;

import iterators.CacheCandlesIterator;
import model.*;
import settings.*;
import terminals.ITerminalGatewaysFactory;

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
