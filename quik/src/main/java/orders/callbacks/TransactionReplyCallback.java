package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.dictionary.ResponseCode;
import orders.model.*;
import protocols.QuikTransactionsGateway;
import tools.Log;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class TransactionReplyCallback implements Trans2QuikLibrary.TransactionReplyCallback {

	private QuikTransactionsGateway gateway;

	@Override
	public void callback(NativeLong resultCode,
						 NativeLong extendedErrorCode,
						 NativeLong replyCode,
						 int transactionId,
						 double orderNumber,
						 String replyMessage) {
		// TODO according to specification there should be transReplyDescriptor as last param

		Log.info("Order submission callback received for transactionId " + transactionId);

		try {
			Transaction transaction = gateway.findTransactionBy(transactionId);

			if (ResponseCode.isSucceed(resultCode.longValue())) {
				transaction.submitionSucceed();
				return;
			}

			Log.error("Submission failed with status " +
					ResponseCode.getBy(resultCode.longValue()).getValue() +
					" and message " + replyMessage);

			transaction.submissionFailed();

		} catch (TransactionNotFound e) {
			Log.error("", e);
		}
	}
}
