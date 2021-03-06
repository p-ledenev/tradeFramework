package trade.terminals.quik.orders.dictionary;

import lombok.Getter;
import trade.core.exceptions.UnsupportedDirection;
import trade.core.model.Order;


/**
 * Created by pledenev on 08.03.2016.
 */
public enum Operation {

	Buy("B"), Sell("S");

	@Getter
	private String value;

	Operation(String value) {
		this.value = value;
	}

	public static String findFor(Order order) throws Throwable {
		if (order.isSell())
			return Sell.value;

		if (order.isBuy())
			return Buy.value;

		throw new UnsupportedDirection(order.getDirection());
	}
}
