package trade.terminals.core.run;

import lombok.AllArgsConstructor;
import org.joda.time.*;
import trade.core.model.*;
import trade.core.tools.*;
import trade.terminals.core.iterators.*;
import trade.terminals.core.settings.*;

import java.util.*;

/**
 * Created by ledenev.p on 02.06.2015.
 */

@AllArgsConstructor
public class Trader {

	public static LocalTime tradeFrom = new LocalTime(10, 4);
	public static LocalTime tradeTo = new LocalTime(23, 48);

	public static LocalTime mainClearingFrom = new LocalTime(18, 43);
	public static LocalTime mainClearingTo = new LocalTime(19, 4);

	public static LocalTime dayClearingFrom = new LocalTime(13, 58);
	public static LocalTime dayClearingTo = new LocalTime(14, 5);

	public static int beginOperationSecond = 45;

	private ICandlesIterator candlesIterator;
	private IOrdersExecutor executor;
	private PortfoliosInitializer initializer;

	public void trade() throws InterruptedException {

		Log.info("Hello!");

		if (!isTradeTime())
			suspendProcessing();

		IPortfolioCandlesIterator iterator = new PortfolioCandlesInitializer(candlesIterator);

		try {
			process(iterator);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		suspendProcessing();

		iterator = new PortfolioCandlesIterator(candlesIterator);
		while (true) {
			try {
				process(iterator);
			} catch (Throwable e) {
				Log.error(e);
			}

			suspendProcessing();
		}
	}

	private void process(IPortfolioCandlesIterator iterator) throws Throwable {

		List<Order> orders = new ArrayList<Order>();
		for (Portfolio portfolio : initializer.getPortfolios()) {
			List<Candle> candles = iterator.getNextCandlesFor(portfolio);
			portfolio.addOrderTo(orders, candles);
		}

		if (orders.size() > 0)
			Log.info("Orders to execute");
		orders.forEach(Order::print);

		List<Order> opposites = findOppositeOrdersIn(orders);

		if (opposites.size() > 0)
			Log.info("Opposites orders");
		opposites.forEach(Order::print);

		orders = removeExecuted(orders);
		executor.execute(orders);

		boolean needSubmitTradeData = opposites.size() > 0;

		for (Order order : opposites)
			order.applyToMachine();

		for (Order order : orders) {
			order.applyToMachine();

			if (order.isProcessed())
				needSubmitTradeData = true;
		}

		OrdersLogger.log(orders);
		OrdersLogger.log(opposites);

		if (initializer.initFileWasModified()) {
			initializer.reread();
			needSubmitTradeData = true;
		}

		if (needSubmitTradeData)
			initializer.write();

		logBlockedMachines();
		checkVolumeDifferences();
	}

	private void checkVolumeDifferences() throws Throwable {

		Set<String> securities = extractSecurities();
		for (String security : securities) {

			int portfoliosVolume = computeVolumeFor(security);
			int terminalVolume = executor.loadVolumeFor(security);

			if (portfoliosVolume == terminalVolume)
				continue;

			Log.info("WARNING! For security " + security + " Terminal has " +
					terminalVolume + ", but portfolios have " + portfoliosVolume);
		}
	}

	private int computeVolumeFor(String security) {

		int volume = 0;
		for (Portfolio portfolio : initializer.getPortfolios())
			if (portfolio.hasSecurity(security))
				volume += portfolio.getSignVolume();

		return volume;
	}

	private Set<String> extractSecurities() {
		Set<String> securities = new HashSet<String>();

		for (Portfolio portfolio : initializer.getPortfolios())
			securities.add(portfolio.getSecurity());

		return securities;
	}

	private void logBlockedMachines() {
		initializer.getPortfolios().forEach(Portfolio::printBlockedMachines);
	}

	private List<Order> findOppositeOrdersIn(List<Order> orders) {

		List<Order> opposites = new ArrayList<>();

		for (Order order : orders) {
			for (Order opposite : orders) {
				if (order.isOppositeTo(opposite)) {
					opposites.add(opposite);
					opposites.add(order);

					order.executed();
					opposite.executed();
				}
			}
		}

		return opposites;
	}

	private List<Order> removeExecuted(List<Order> orders) {

		List<Order> ordersToRemove = new ArrayList<>();

		for (Order order : orders)
			if (order.isExecuted())
				ordersToRemove.add(order);

		ordersToRemove.forEach(orders::remove);

		return orders;
	}

	private void suspendProcessing() throws InterruptedException {

		DateTime nowDate = DateTime.now();

		DateTime tradeBegin = nowDate.withTime(tradeFrom);
		DateTime tradeEnd = nowDate.withTime(tradeTo);

		DateTime mainClearingBegin = nowDate.withTime(mainClearingFrom);
		DateTime mainClearingEnd = nowDate.withTime(mainClearingTo);

		DateTime dayClearingBegin = nowDate.withTime(dayClearingFrom);
		DateTime dayClearingEnd = nowDate.withTime(dayClearingTo);

		DateTime wakeUpTime = nowDate.withSecondOfMinute(beginOperationSecond);
		if (nowDate.getSecondOfMinute() > 43)
			wakeUpTime = wakeUpTime.plusMinutes(1);

		if (nowDate.isAfter(mainClearingBegin) && nowDate.isBefore(mainClearingEnd))
			wakeUpTime = mainClearingEnd.withSecondOfMinute(beginOperationSecond);

		if (nowDate.isAfter(dayClearingBegin) && nowDate.isBefore(dayClearingEnd))
			wakeUpTime = dayClearingEnd.withSecondOfMinute(beginOperationSecond);

		if (nowDate.isBefore(tradeBegin))
			wakeUpTime = tradeBegin.withSecondOfMinute(beginOperationSecond);

		if (nowDate.isAfter(tradeEnd)) {
			int days = (nowDate.getDayOfWeek() == DateTimeConstants.FRIDAY) ? 3 : 1;
			wakeUpTime = tradeBegin.plusDays(days).withSecondOfMinute(beginOperationSecond);
		}

		if (nowDate.getDayOfWeek() == DateTimeConstants.SATURDAY)
			wakeUpTime = tradeBegin.plusDays(2).withSecondOfMinute(beginOperationSecond);

		if (nowDate.getDayOfWeek() == DateTimeConstants.SUNDAY)
			wakeUpTime = tradeBegin.plusDays(1).withSecondOfMinute(beginOperationSecond);

		Log.info("wake up " + Format.asString(wakeUpTime));

		Thread.sleep(wakeUpTime.getMillis() - nowDate.getMillis());
	}

	private boolean isTradeTime() {
		DateTime nowDate = DateTime.now();

		DateTime tradeBegin = nowDate.withTime(tradeFrom);
		DateTime tradeEnd = nowDate.withTime(tradeTo);

		if (nowDate.isBefore(tradeBegin) || nowDate.isAfter(tradeEnd))
			return false;

		if (nowDate.getDayOfWeek() == DateTimeConstants.SATURDAY ||
				nowDate.getDayOfWeek() == DateTimeConstants.SUNDAY)
			return false;

		return true;
	}
}
