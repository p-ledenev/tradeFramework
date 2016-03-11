package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.model.Trans2QuikLibrary;
import protocols.QuikOrdersGateway;

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


	}
}
