package model;

import java.util.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class TryOutOrdersExecutor implements IOrdersExecutor {

	private int tradeYear;

	public TryOutOrdersExecutor(int tradeYear) {
		this.tradeYear = tradeYear;
	}

	public void execute(List<Order> orders) {

		for (Order order : orders) {

			Candle next = order.getLastCandle();
			if (!next.hasYearAs(tradeYear))
				continue;

			order.setValue(((TryOutCandle) next).getNextValue());
			order.executed();
		}
	}

	public void checkVolumeFor(String security, int volume) throws Throwable {

	}
}
