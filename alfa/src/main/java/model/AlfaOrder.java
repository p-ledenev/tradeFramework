package model;

import exceptions.*;
import lombok.Data;
import tools.Log;

import java.util.Arrays;

/**
 * Created by ledenev.p on 11.06.2015.
 */

@Data
public class AlfaOrder {

    public static int maxCheckAttempts = 10;

    private AlfaGateway gateway;

    private Order order;
    private AlfaOrderStatus status;
    private int checkAttemptCounter;
    private Integer orderNumber;

    public AlfaOrder(Order order, AlfaGateway gateway) {
        this.order = order;
        this.gateway = gateway;

        status = AlfaOrderStatus.newest;
        checkAttemptCounter = 0;
    }

    public String printStatus() {

        String message = "";
        if (status == AlfaOrderStatus.newest)
            message = "OperateStock: Can not execute order. Not enough money.";

        if (status == AlfaOrderStatus.executedPartly)
            message = "OperateStock: Order No " + orderNumber + " executed partly. This machine should be blocked.";

        if (status == AlfaOrderStatus.executionStatusNotObtained)
            message = "OperateStock: Order No " + orderNumber + " status not obtained. This machine should be blocked.";

        if (status == AlfaOrderStatus.submissionStatusNotObtained)
            message = "OperateStock: Can not create order. Order Id was not obtained during submission. This machine should be blocked.";

        if (status == AlfaOrderStatus.submissionBlocked)
            message = "OperateStock: Can not create order. Order submission blocked. This machine continue working";

        if (status == AlfaOrderStatus.deleted)
            message = "OperateStock: Order No " + orderNumber + " was deleted. This machine continue working";

        if (status == AlfaOrderStatus.executionSucceed)
            message = "OperateStock: " + toString() + " succeed";

        return message;
    }

    public boolean allowExecution() {
        if (shouldBeBlocked() || isExecutionSucceed())
            return false;

        if (isMaxCheckAttemptsExceeded())
            return false;

        return true;
    }

    public boolean isNewest() {
        return status == AlfaOrderStatus.newest;
    }

    public boolean isSubmissionSucceed() {
        return status == AlfaOrderStatus.submissionSucceed;
    }

    public boolean isExecutionSucceed() {
        return status == AlfaOrderStatus.executionSucceed;
    }

    public boolean isSubmissionBlocked() {
        return status == AlfaOrderStatus.submissionBlocked;
    }

    public void executed() {
        order.executed();
        status = AlfaOrderStatus.executionSucceed;
    }

    public void blockSubmission() {
        status = AlfaOrderStatus.submissionBlocked;
    }

    public boolean shouldBeDeleted() {
        AlfaOrderStatus[] failedStatuses = {AlfaOrderStatus.executedPartly, AlfaOrderStatus.executionStatusNotObtained};

        if (Arrays.asList(failedStatuses).contains(status))
            return true;

        return false;
    }

    public boolean shouldBeBlocked() {
        return (shouldBeDeleted() || AlfaOrderStatus.submissionStatusNotObtained.equals(status));
    }

    public boolean isMaxCheckAttemptsExceeded() {
        return checkAttemptCounter > maxCheckAttempts;
    }

    public void loadLastValue() {
        try {
            double lastValue = gateway.loadLastValueFor(order.getSecurity());
            order.setValue(lastValue + (order.isBuy() ? 0.002 : -0.002) * lastValue);

        } catch (LoadLastValueFailure e) {
            status = AlfaOrderStatus.submissionBlocked;

        } catch (AlfaGatewayFailure e) {
            Log.info(toString() + " " + e.getMessage());
        }
    }

    public void submit() {

        if (status != AlfaOrderStatus.newest)
            return;

        try {
            orderNumber = gateway.submit(this);
            status = AlfaOrderStatus.submissionSucceed;

        } catch (UnsupportedDirection e) {
            status = AlfaOrderStatus.submissionBlocked;

        } catch (OrderSubmissionFailure e) {
            status = AlfaOrderStatus.submissionStatusNotObtained;

        } catch (AlfaGatewayFailure e) {
            Log.info(toString() + " " + e.getMessage());
        }
    }

    public AlfaOrderDirection getAlfaDirection() throws UnsupportedDirection {
        if (order.isBuy())
            return AlfaOrderDirection.Buy;

        if (order.isSell())
            return AlfaOrderDirection.Sell;

        throw new UnsupportedDirection("Order has no direction");
    }

    public boolean executionAllowed() {

        if (shouldBeBlocked() || isExecutionSucceed() || isSubmissionBlocked())
            return false;

        if (isMaxCheckAttemptsExceeded())
            return false;

        return true;
    }

    public void executedWith(double value) {
        order.setValue(value);
        executed();
    }

    public boolean isOppositeTo(AlfaOrder alfaOrder) {
        return order.hasSameSecurity(alfaOrder.getOrder()) && order.hasOppositeDirectionTo(alfaOrder.getOrder()) && isNewest() && alfaOrder.isNewest();
    }

    public void dropIfNecessary() throws AlfaGatewayFailure {

        if (shouldBeBlocked())
            order.blockMachine();

        if (!shouldBeDeleted())
            return;

        gateway.dropOrder(this.getOrderNumber());

        status = AlfaOrderStatus.deleted;
        order.unblockMachine();
    }

    public void loadStatus() {

        checkAttemptCounter++;

        try {
            String result = gateway.loadStatus(orderNumber);

            String[] deals = result.split("\\n|\\r");

            double totalValue = 0;
            int totalVolume = 0;
            for (String strDeal : deals) {
                String[] deal = strDeal.split("\\|");

                int dealVolume = Integer.parseInt(deal[1]);
                totalValue += Double.parseDouble(deal[0]) * dealVolume;
                totalVolume += dealVolume;
            }

            if (order.getVolume() != totalVolume) {
                status = AlfaOrderStatus.executedPartly;
                return;
            }

            order.setValue(totalValue / totalVolume);
            executed();

        } catch (LoadOrderStatusFailure e) {
            if (isMaxCheckAttemptsExceeded())
                status = AlfaOrderStatus.executionStatusNotObtained;

        } catch (AlfaGatewayFailure e) {
            Log.info(toString() + " " + e.getMessage());
        }
    }

    public String getSecurity() {
        return order.getSecurity();
    }

    public int getVolume() {
        return order.getVolume();
    }

    public double getValue() {
        return order.getValue();
    }
}
