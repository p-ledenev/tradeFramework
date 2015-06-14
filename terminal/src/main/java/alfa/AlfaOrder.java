package alfa;

import model.Order;
import tools.Log;

import java.util.Arrays;

/**
 * Created by ledenev.p on 11.06.2015.
 */

public class AlfaOrder extends Order{

    public static int maxCheckAttempts = 10;

    private AlfaGateway gateway;

    private AlfaOrderStatus status;
    private int checkAttemptCounter;
    private Integer orderNumber;

    public AlfaOrder(Order order, AlfaGateway gateway) {
        super(order.getNewPosition(), order.getMachine());
        this.gateway = gateway;

        status = AlfaOrderStatus.newest;
        checkAttemptCounter = 0;
    }

    public String printStatus() {
        String message = null;
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

        if (isMaxCheckAttemtsExeeded())
            return false;

        return true;
    }

    public boolean isNewest() {
        return status == AlfaOrderStatus.newest;
    }

    public boolean isSubmissionFailed() {
        return status == AlfaOrderStatus.submissionStatusNotObtained;
    }

    public boolean isSubmissionBlocked() {
        return status == AlfaOrderStatus.submissionBlocked;
    }

    public boolean isSubmissionSucceed() {
        return status == AlfaOrderStatus.submissionSucceed;
    }

    public boolean isExecutionSucceed() {
        return status == AlfaOrderStatus.executionSucceed;
    }

    public boolean isDeleted() {
        return status == AlfaOrderStatus.deleted;
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

    public boolean isMaxCheckAttemtsExeeded() {
        return checkAttemptCounter > maxCheckAttempts;
    }

    public void submit() {
        if (status != AlfaOrderStatus.newest)
            return;

        double lastValue = 0;
        try {
            lastValue = gateway.loadLastValueFor(machine.getSecurity());
        }
        catch(AlfaGatewayFailure e) {
            Log.error("Submission failed for order " + toString() + " with message " + e.getMessage());
            status = AlfaOrderStatus.submissionBlocked;

            return;
        }

        lastValue = (getDirection().)

        if (getD.isBuy())
            lastValue += 0.002 * lastValue; // открытие

        if (trade.isSell())
            trade.value -= 0.002 * trade.value; // закрытие

        // если предыдущий не завершен
        if (!isParenetExecutionSucceed()) return;

        trade.volume = (trade.volume > 0) ? trade.volume : machine.parent.lot;

        if (trade.volume <= 0)
        {
            AlfaDirectGateway.printInfo(DateTime.Now, machine, "CreateOrder: Faild. Not enough money in Portfolio. Order volume less 0.");
            status = OrderStatus.tenderedBlock;

            return;
        }

        // подать ордер
        orderNumber = gateway.createOrder(this);

        status = (orderNumber > 0) ? OrderStatus.tenderedSuccess : OrderStatus.tenderedFail;
    }
}
