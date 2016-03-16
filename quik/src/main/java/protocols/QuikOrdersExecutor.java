package protocols;

import model.*;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersExecutor implements IOrdersExecutor {

	private QuikTransactionsGateway gateway;

	public QuikOrdersExecutor(QuikTransactionsGateway gateway) throws Throwable {
		this.gateway = gateway;

		gateway.connect();
		gateway.registerCallbacks();
	}

	@Override
	public void execute(List<Order> orders) throws Throwable {

		gateway.submitTransactionsBy(orders);

		Thread.sleep(20 * 1000);

		if (gateway.hasUnfinishedTransactions()) {
			gateway.dropUnfinishedTransactions();
			Thread.sleep(10 * 1000);
		}

		gateway.finalizeOrders();
		gateway.cleanOrdersQueue();
	}

	@Override
	public void checkVolumeFor(String security, int volume) throws Throwable {

	}
}
