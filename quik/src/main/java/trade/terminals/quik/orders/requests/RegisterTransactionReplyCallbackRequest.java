package trade.terminals.quik.orders.requests;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import trade.terminals.quik.orders.jnative.Trans2QuikLibrary.TransactionReplyCallback;

/**
 * Created by pledenev on 07.03.2016.
 */

@AllArgsConstructor
public class RegisterTransactionReplyCallbackRequest extends QuikRequest {

    private TransactionReplyCallback transactionReplyCallback;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SET_TRANSACTIONS_REPLY_CALLBACK(
                transactionReplyCallback,
                errorCode,
                errorMessage,
                errorMessage.length);
    }
}
