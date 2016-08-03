package trade.terminals.quik.orders.dictionary;

/**
 * Created by pledenev on 07.03.2016.
 */
public enum OrderStatus {

	Active(1), Cancelled(2), Executed(3);

	private int value;

	public static OrderStatus getBy(Long code) {
		for (OrderStatus status : OrderStatus.values())
			if (status.hasValue(code))
				return status;

		return Executed;
	}

	public static boolean isExecuted(Long status) {
		return getBy(status).equals(Executed);
	}

	public static boolean isActive(Long status) {
		return getBy(status).equals(Active);
	}

	public static boolean isCancelled(Long status) {
		return getBy(status).equals(Cancelled);
	}

	private boolean hasValue(Long code) {
		return value == code.intValue();
	}

	private OrderStatus(int value) {
		this.value = value;
	}
}
