package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.dictionary.ResponseCode;
import orders.model.*;
import tools.Log;

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
		// TODO according to specification there should be transReplyDescriptor as last param

		Log.info("Order submission callback received for transactionId " + transactionId +
				" with status " + ResponseCode.getBy(resultCode.longValue()));

		try {
			Transaction transaction = queue.findBy(transactionId);

			if (ResponseCode.isSucceed(resultCode.longValue())) {
				transaction.submissionSucceed();
				return;
			}

			Log.error("Submission failed with message " + replyMessage);

			transaction.submissionFailed();

		} catch (TransactionNotFound e) {
			Log.error("", e);
		}
	}
}
