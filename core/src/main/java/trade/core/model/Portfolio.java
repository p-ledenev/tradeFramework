package trade.core.model;

import lombok.*;
import org.joda.time.*;
import trade.core.tools.Log;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Getter @Setter
public class Portfolio implements IMoneyStateSupport {

	public static LocalTime intradayCloseDayPositionTime = new LocalTime(18, 33);
	public static LocalTime intradayOpenEveningPositionTime = new LocalTime(19, 1);

	private CandlesStorage candlesStorage;
	private boolean intradayTrading;
	private List<Machine> machines;
	private String security;
	private String title;
	private int lot;

	public Portfolio(String title, String security, int lot, boolean isIntraday, CandlesStorage candlesStorage) {
		this.title = title;
		this.security = security;

		this.lot = lot;
		this.candlesStorage = candlesStorage;
		this.intradayTrading = isIntraday;

		machines = new ArrayList<>();
	}

	public Portfolio(String title, String security, boolean isIntraday, CandlesStorage candlesStorage) {
		this(title, security, 100, isIntraday, candlesStorage);
	}

	public void addMachine(Machine machine) {
		machines.add(machine);
	}

	public void addOrderTo(final List<Order> orders, List<Candle> candles) throws Throwable {
		if (candles.isEmpty())
			return;

		candlesStorage.addOnlyNew(candles);

		Candle last = candles.get(candles.size() - 1);
		boolean dayClosePositionTime = last.hasLastTimeWithIn(intradayCloseDayPositionTime, intradayOpenEveningPositionTime);

		if (dayClosePositionTime && intradayTrading) {
			machines.forEach(machine -> machine.addClosePositionOrderTo(orders));
		} else {
			machines.forEach(machine -> machine.addOrderTo(orders));
		}
	}

	public String printStrategy() {
		return machines.get(0).getDecisionStrategyName();
	}

	public MoneyState getCurrentState() {
		return new MoneyState(getLatestTime(), computeCurrentMoney());
	}

	private double computeCurrentMoney() {
		double currentMoney = 0;
		for (Machine machine : machines)
			currentMoney += machine.getCurrentMoney();

		return currentMoney / machines.size();
	}

	private DateTime getLatestTime() {
		DateTime date = machines.get(0).getPositionDate();

		for (Machine machine : machines)
			if (date.isBefore(machine.getPositionDate()))
				date = machine.getPositionDate();

		return date;
	}

	public int countMachines() {
		return machines.size();
	}

	public int computeInitialCandlesSize() {
		int size = 0;
		for (Machine machine : machines)
			if (size < machine.getInitialStorageSize())
				size = machine.getInitialStorageSize();

		return size;
	}

	public int computeStorageSizeFor(List<Candle> candles) {
		return candlesStorage.computeStorageSizeFor(candles);
	}

	public Candle getLastCandle() {
		return candlesStorage.last();
	}

	public String getDecisonStrategyName() {
		return machines.get(0).getDecisionStrategyName();
	}

	public void printBlockedMachines() {
		for (Machine machine : machines)
			if (machine.isBlocked())
				Log.info("WARNING! " + title + " " + machine.getDepth() + " is blocked");
	}

	public boolean hasSecurity(String security) {
		return this.security.equals(security);
	}

	public int getSignVolume() {
		int volume = 0;
		for (Machine machine : machines)
			volume += machine.getSignVolume();

		return volume;
	}

	public Machine getMachine(int i) {
		return machines.get(i);
	}

	public Double getSieveParam() {
		return machines.get(0).getDecisionStrategy().getSieveParam();
	}

	public Integer getFillingGapsNumber() {
		return machines.get(0).getDecisionStrategy().getFillingGapsNumber();
	}

	public String getDescription() {
		return getDecisonStrategyName() + " " + getSecurity() + " " + getSieveParam() + " " + getFillingGapsNumber()
				+ (intradayTrading ? " intraday" : " continuous");
	}

	public int getMaxDepth() {
		int maxDepth = 0;

		for (Machine machine : machines)
			if (machine.getDepth() > maxDepth)
				maxDepth = machine.getDepth();

		return maxDepth;
	}
}
