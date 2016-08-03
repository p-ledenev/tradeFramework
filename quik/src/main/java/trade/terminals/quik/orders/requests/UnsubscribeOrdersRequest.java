package trade.terminals.quik.orders.requests;

import com.sun.jna.NativeLong;

/**
 * Created by machineL on 17.03.2016.
 */
public class UnsubscribeOrdersRequest extends QuikRequest {

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_UNSUBSCRIBE_ORDERS();
    }
}
