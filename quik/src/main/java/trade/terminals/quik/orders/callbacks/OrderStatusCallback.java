package trade.terminals.quik.orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import trade.terminals.quik.orders.dictionary.OrderStatus;
import trade.terminals.quik.orders.jnative.Trans2QuikLibrary;
import trade.terminals.quik.orders.model.*;
import trade.core.tools.Log;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class OrderStatusCallback implements Trans2QuikLibrary.OrderStatusCallback {

	private TransactionsQueue queue;

	//@Override
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

		Log.debug("Order status callback received for transactionId " + transactionId +
				" with status " + OrderStatus.getBy(status.longValue()) +
				"; orderNumber " + Double.valueOf(orderNumber).longValue() +
				"; classCode " + classCode +
				"; securityCode " + securityCode +
				"; value " + value +
				"; restVolume " + restVolume +
				"; totalValue " + totalValue + "" +
				"; isSell " + isSell +
				"; mode " + mode);

		if (mode.longValue() == 1L || mode.longValue() == 2L)
			return;

		try {
			Transaction transaction = queue.findBy(transactionId);

			if (isExecuted(status, restVolume))
				transaction.executed(value);

			if (isExecutedPartly(restVolume, transaction))
				transaction.executedPartly();

			if (isDeleted(status, transaction))
				transaction.deletionSucceed();

		} catch (TransactionNotFound e) {
			Log.debug(e.getMessage());
		}
	}

	private boolean isDeleted(NativeLong status, Transaction transaction) {
		return OrderStatus.isCancelled(status.longValue()) && transaction.isSubmissionSucceed();
	}

	private boolean isExecutedPartly(NativeLong restVolume, Transaction transaction) {
		return !isZero(restVolume) && !transaction.hasSameVolume((int) restVolume.longValue());
	}

	private boolean isExecuted(NativeLong status, NativeLong restVolume) {
		return OrderStatus.isExecuted(status.longValue()) && isZero(restVolume);
	}

	private boolean isZero(NativeLong volume) {
		return volume.longValue() == 0L;
	}
}
