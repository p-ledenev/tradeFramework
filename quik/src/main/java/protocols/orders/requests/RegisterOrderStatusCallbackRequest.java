package protocols.orders.requests;

import com.sun.jna.*;
import lombok.AllArgsConstructor;
import protocols.orders.Trans2QuikLibrary.OrderStatusCallback;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class RegisterOrderStatusCallbackRequest extends QuikRequest {

    private OrderStatusCallback orderStatusCallback;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SET_TRANSACTIONS_REPLY_CALLBACK(orderStatusCallback, errorCode, errorMessage, errorMessage.length);
    }
}
