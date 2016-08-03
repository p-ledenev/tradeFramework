package trade.terminals.quik.protocols;


import trade.core.model.*;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersExecutor implements IOrdersExecutor {

	private QuikTransactionsGateway gateway;

	public QuikOrdersExecutor(QuikTransactionsGateway gateway) throws Throwable {

		System.setProperty("jna.encoding", "cp1251");

		this.gateway = gateway;

		gateway.connect();
		gateway.registerCallbacks();
	}

	@Override
	public void execute(List<Order> orders) throws Throwable {

		if (orders.size() <= 0)
			return;

		gateway.submitTransactionsBy(orders);

		Thread.sleep(15 * 1000);

		if (gateway.hasUnfinishedTransactions()) {
			gateway.dropUnfinishedTransactions();
			Thread.sleep(5 * 1000);
		}

		gateway.finalizeOrders();
		gateway.cleanOrdersQueue();
	}

	@Override
	public int loadVolumeFor(String security) throws Throwable {
		return gateway.loadVolumeFor(security);
	}
}
