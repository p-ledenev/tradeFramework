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

	public int loadVolumeFor(String security) throws Throwable {
		return 0;
	}
}
