package trade.terminals.quik.orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import trade.terminals.quik.orders.dictionary.ResponseCode;
import trade.terminals.quik.orders.jnative.Trans2QuikLibrary;
import trade.terminals.quik.orders.model.*;
import trade.core.tools.Log;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class TransactionReplyCallback implements Trans2QuikLibrary.TransactionReplyCallback {

	private TransactionsQueue queue;

	@Override
	public void callback(NativeLong resultCode,
						 NativeLong extendedErrorCode,
						 NativeLong replyCode,
						 int transactionId,
						 double orderNumber,
						 String replyMessage) {

		Log.debug("Order submission callback received for transactionId " + transactionId +
				" with status " + ResponseCode.getBy(resultCode.longValue()) +
				"; replyCode " + replyCode.longValue() + "; orderNumber " + Double.valueOf(orderNumber).longValue() +
				"; replyMessage " + replyMessage);

		try {
			Transaction transaction = queue.findBy(transactionId);

			if (ResponseCode.isSucceed(resultCode.longValue()) && orderNumber != 0.) {
				transaction.submissionSucceed(Double.valueOf(orderNumber).longValue());
				return;
			}

			Log.error("Submission failed with message " + replyMessage);

			transaction.submissionFailed();

		} catch (TransactionNotFound e) {
			Log.debug(e.getMessage());
		}
	}
}
