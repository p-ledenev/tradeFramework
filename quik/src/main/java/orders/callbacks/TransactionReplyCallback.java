package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.dictionary.ReturnCodes;
import orders.model.*;
import protocols.QuikOrdersGateway;
import tools.Log;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class TransactionReplyCallback implements Trans2QuikLibrary.TransactionReplyCallback {

	private QuikOrdersGateway gateway;

	@Override
	public void callback(NativeLong resultCode,
						 NativeLong extendedErrorCode,
						 NativeLong replyCode,
						 int transactionId,
						 double orderNumber,
						 String replyMessage) {

		Log.info("Order submission callback received for transactionId " + transactionId);

		try {
			Transaction transaction = gateway.findTransactionBy(transactionId);

			if (ReturnCodes.)

		} catch (TransactionNotFound e) {
			Log.error("", e);
		}
	}
}
