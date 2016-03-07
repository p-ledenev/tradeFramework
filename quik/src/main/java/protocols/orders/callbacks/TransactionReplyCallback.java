package protocols.orders.callbacks;

import com.sun.jna.NativeLong;
import protocols.orders.Trans2QuikLibrary;

/**
 * Created by dlede on 07.03.2016.
 */
public class TransactionReplyCallback implements Trans2QuikLibrary.TransactionReplyCallback {

    @Override
    public void callback(NativeLong resultCode,
                         NativeLong extendedErrorCode,
                         NativeLong replyCode,
                         int transactionId,
                         double orderNumber,
                         String replyMessage) {



    }
}
