package alfa;

import lombok.Data;
import model.Order;

import java.util.Arrays;

/**
 * Created by ledenev.p on 11.06.2015.
 */

@Data
public class AlfaOrder {

    public static int maxCheckAttempts = 10;

    private Order order;

    private AlfaOrderStatus status;
    private int checkAttemptCounter;
    private Integer orderNumber;

    public AlfaOrder(Order order) {
        this.order = order;

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
            message = "OperateStock: " + order.toString() + " succeed";

        return message;
    }

    public boolean allowExecution() {
        if (shouldBeBlocked() || isExecutionSucceed())
            return false;

        if (isMaxCheckAttemtsExeeded())
            return false;

        return true;
    }

    public boolean hasSameSecurity(AlfaOrder alfaOrder) {
        return order.hasSameSecurity(alfaOrder.getOrder());
    }

    public boolean hasOppositeDirection(AlfaOrder alfaOrder) {
        return order.hasOppositeDirection(alfaOrder.getOrder());
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

    public boolean shouldBeDeleted() {
        AlfaOrderStatus[] failedStatuses = {AlfaOrderStatus.executedPartly, AlfaOrderStatus.executionStatusNotObtained};

        if (Arrays.asList(failedStatuses).contains(status))
            return true;

        return false;
    }

    public boolean shouldBeBlocked() {
        return (shouldBeDeleted() || AlfaOrderStatus.submissionStatusNotObtained.equals(status));
    }

    public boolean isDeleted() {
        return status == AlfaOrderStatus.deleted;
    }

    public boolean isMaxCheckAttemtsExeeded() {
        return checkAttemptCounter > maxCheckAttempts;
    }
}
