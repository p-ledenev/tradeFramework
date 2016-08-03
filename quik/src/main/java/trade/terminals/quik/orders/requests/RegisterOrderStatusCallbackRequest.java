package trade.terminals.quik.orders.requests;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import trade.terminals.quik.orders.callbacks.OrderStatusCallback;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class RegisterOrderStatusCallbackRequest extends QuikRequest {

    private OrderStatusCallback orderStatusCallback;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_START_ORDERS(orderStatusCallback);
    }
}
