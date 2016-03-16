package run;

import iterators.*;
import lombok.AllArgsConstructor;
import model.*;
import org.joda.time.*;
import settings.*;
import tools.*;

import java.time.LocalTime;
import java.util.*;

/**
 * Created by ledenev.p on 02.06.2015.
 */

@AllArgsConstructor
public class Trader {

	public static LocalTime tradeTo = LocalTime.of(23, 50);

	private ICandlesIterator candlesIterator;
	private IOrdersExecutor executor;
	private PortfoliosInitializer initializer;

	public void trade() throws Throwable {

		IPortfolioCandlesIterator iterator = new PortfolioCandlesInitializer(candlesIterator);

		Log.disableDebug();
		process(iterator);
		Log.enableDebug();

		suspendProcessing();

		iterator = new PortfolioCandlesIterator(candlesIterator);
		while (true) {
			process(iterator);
			suspendProcessing();
		}
	}

	private void process(IPortfolioCandlesIterator iterator) throws Throwable {

		List<Order> orders = new ArrayList<Order>();
		for (Portfolio portfolio : initializer.getPortfolios()) {
			List<Candle> candles = iterator.getNextCandlesFor(portfolio);
			portfolio.addOrderTo(orders, candles);
		}

		Log.info("Orders to execute");
		orders.forEach(Order::print);

		List<Order> opposites = findOppositeOrdersIn(orders);

		Log.info("\nOpposites orders");
		opposites.forEach(Order::print);

		orders = removeExecuted(orders);
		executor.execute(orders);

		boolean needSubmitTradeData = opposites.size() > 0;
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
			int volume = computeVolumeFor(security);

			executor.checkVolumeFor(security, volume);
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
		for (Order order : orders)
			if (order.isExecuted())
				orders.remove(order);

		return orders;
	}

	private void suspendProcessing() throws Throwable {

		DateTime date = DateTime.now();

		DateTime tradeBegin = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 10, 4, 50);
		DateTime tradeEnd = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), tradeTo.getHour(), tradeTo.getMinute(), 0);

		DateTime tradePauseBegin = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 18, 45, 0);
		DateTime tradePauseEnd = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 19, 0, 0);

		Period rclock = new Period(0, 0, 0, 0);
		Period postfix = new Period(10, 4, 50, 0);
		Period prefix = new Period(23 - date.getHourOfDay(), 59 - date.getMinuteOfHour(), 59 - date.getSecondOfMinute(), 0);

		boolean weekend = (date.getDayOfWeek() == DateTimeConstants.SATURDAY || date.getDayOfWeek() == DateTimeConstants.SUNDAY);

		if (date.getDayOfWeek() == DateTimeConstants.SATURDAY)
			rclock = new Period(24, 0, 0, 0);

		if (date.compareTo(tradeBegin) >= 0 && date.compareTo(tradeEnd) <= 0 && !weekend) {
			postfix = new Period(0, 0, 0, 0);

			int upSeconds = 50;
			int delta = (date.getSecondOfMinute() >= upSeconds) ? 60 - date.getSecondOfMinute() : -date.getSecondOfMinute();
			prefix = new Period(0, 0, upSeconds + delta, 0);
		}

		if (date.compareTo(tradePauseBegin) >= 0 && date.compareTo(tradePauseEnd) <= 0) {
			prefix = new Period(0, 61 - date.getMinuteOfHour(), 0, 0);
		}

		if (date.compareTo(tradeBegin) < 0) {
			prefix = new Period(0, 0, 0, 0);
			postfix = new Period(10 - date.getHourOfDay(), 04 - date.getMinuteOfHour(), 50 - date.getSecondOfMinute(), 0);
		}

		Log.info("wake up " + Format.asString(date.plus(rclock).plus(prefix).plus(postfix)));

		Thread.sleep(rclock.plus(prefix).plus(postfix).toStandardDuration().getMillis());
	}
}
