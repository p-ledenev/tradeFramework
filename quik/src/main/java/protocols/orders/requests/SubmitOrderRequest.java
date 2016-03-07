package protocols.orders.requests;

import com.sun.jna.NativeLong;

/**
 * Created by dlede on 07.03.2016.
 */
public class SubmitOrderRequest extends QuikRequest {

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SEND_ASYNC_TRANSACTION()
    }
}
