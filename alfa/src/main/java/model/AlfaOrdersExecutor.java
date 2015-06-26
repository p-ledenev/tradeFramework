package model;

import exceptions.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class AlfaOrdersExecutor implements IOrdersExecutor {

    private AlfaGateway gateway;

    public AlfaOrdersExecutor(AlfaGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(List<Order> orders) throws InterruptedException {

        List<AlfaOrder> alfaOrders = wrap(orders);

        Log.info("Trying execute opposite orders");
        executeOppositeOrders(alfaOrders);

        Log.info("Trying execute all other orders");
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

    public void checkVolumeFor(String security, int volume) throws Throwable {

        int alfaVolume = gateway.loadSecurityVolume(security);
        if (alfaVolume == volume)
            return;

        Log.info("For security " + security + " Alfa Terminal has " + alfaVolume + ", but portfolios have " + volume);
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

    private void executeOppositeOrders(List<AlfaOrder> alfaOrders) {
        for (AlfaOrder order : alfaOrders) {
            for (AlfaOrder opposite : alfaOrders) {
                if (order.isOppositeTo(opposite)) {
                    try {
                        Log.info("Mutual execution opposite orders: ");
                        Log.info(order.toString());
                        Log.info(opposite.toString());

                        double lastValue = gateway.loadLastValueFor(order.getSecurity());
                        order.executedWith(lastValue);
                        opposite.executedWith(lastValue);

                    } catch (LoadLastValueFailure e) {
                        Log.info(order.toString() + " " + e.getMessage());

                        order.blockSubmission();
                        opposite.blockSubmission();

                    } catch (AlfaGatewayFailure e) {
                        Log.info(order.toString() + " " + e.getMessage());
                    }
                }
            }
        }
    }
}
