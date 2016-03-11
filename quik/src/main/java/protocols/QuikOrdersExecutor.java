package protocols;

import model.*;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersExecutor implements IOrdersExecutor {

	private QuikTransactionsGateway gateway;

	@Override
	public void execute(List<Order> orders) throws Throwable {

		gateway.submitTransactionsBy(orders);

		// TODO check if callback create new Thread
		Thread.sleep(20 * 1000);

		if (gateway.hasUnfinishedTransactions()) {
			gateway.dropUnfinishedTransactions();
			Thread.sleep(20 * 1000);
		}

		gateway.finalizeOrders();
		gateway.cleanOrdersQueue();
	}

	@Override
	public void checkVolumeFor(String security, int volume) throws Throwable {

	}
}
