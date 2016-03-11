package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.dictionary.OrderStatus;
import orders.model.*;
import protocols.QuikOrdersGateway;
import tools.Log;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class OrderStatusCallback implements Trans2QuikLibrary.OrderStatusCallback {

	private QuikOrdersGateway gateway;

	@Override
	public void callback(NativeLong mode,
						 int transactionId,
						 double orderNumber,
						 String classCode,
						 String securityCode,
						 double value,
						 NativeLong restVolume,
						 double totalValue,
						 NativeLong isSell,
						 NativeLong status,
						 NativeLong orderDescriptor) {

		Log.info("Order status callback received for transactionId " + transactionId);

		try {
			Transaction transaction = gateway.findTransactionBy(transactionId);

			if (isExecuted(status, restVolume))
				transaction.executed();

			if (isActive(status))
				transaction.executedPartly();

			if (isDeleted(status, transaction))
				transaction.deletionSucceed();

		} catch (TransactionNotFound e) {
			Log.error("", e);
		}
	}

	private boolean isDeleted(NativeLong status, Transaction transaction) {
		return OrderStatus.isCancelled(status.longValue()) && transaction.isSubmissionSucceed();
	}

	private boolean isActive(NativeLong status) {
		return OrderStatus.isActive(status.longValue());
	}

	private boolean isExecuted(NativeLong status, NativeLong restVolume) {
		return OrderStatus.isExecuted(status.longValue()) && restVolume.equals(0);
	}
}
