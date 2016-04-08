package model;

import tools.Log;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class AlfaOrdersExecutor implements IOrdersExecutor {

	private AlfaGateway gateway;

	public AlfaOrdersExecutor(AlfaGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public void execute(List<Order> orders) throws Throwable {

		List<AlfaOrder> alfaOrders = wrap(orders);

		Log.info("Trying execute orders");
		while (hasOrdersToProcess(alfaOrders)) {

			if (submit(alfaOrders))
				Thread.sleep(11 * 1000);

			for (AlfaOrder order : alfaOrders)
				order.loadStatus();

			Thread.sleep(2 * 1000);
		}

		for (AlfaOrder order : alfaOrders)
			order.dropIfNecessary();

		for (AlfaOrder order : alfaOrders)
			Log.info(order.toString() + " " + order.printStatus());
	}

	@Override
	public int loadVolumeFor(String security) throws Throwable {
		return gateway.loadSecurityVolume(security);
	}

	private boolean hasOrdersToProcess(List<AlfaOrder> alfaOrders) {
		for (AlfaOrder order : alfaOrders)
			if (order.executionAllowed())
				return true;

		return false;
	}

	private boolean submit(List<AlfaOrder> alfaOrders) {

		boolean isAnySubmitted = false;
		for (AlfaOrder order : alfaOrders) {
			order.loadLastValue();
			order.submit();

			if (order.isSubmissionSucceed())
				isAnySubmitted = true;
		}

		return isAnySubmitted;
	}

	private List<AlfaOrder> wrap(List<Order> orders) {
		List<AlfaOrder> alfaOrders = new ArrayList<AlfaOrder>();

		for (Order order : orders)
			alfaOrders.add(new AlfaOrder(order, gateway));

		return alfaOrders;
	}
}
