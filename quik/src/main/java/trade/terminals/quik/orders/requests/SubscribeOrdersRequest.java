package trade.terminals.quik.orders.requests;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;

/**
 * Created by machineL on 17.03.2016.
 */

@AllArgsConstructor
public class SubscribeOrdersRequest extends QuikRequest {

    private String classCode;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SUBSCRIBE_ORDERS(classCode, "");
    }
}
