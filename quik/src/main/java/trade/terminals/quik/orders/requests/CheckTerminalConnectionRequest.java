package trade.terminals.quik.orders.requests;

import com.sun.jna.NativeLong;

/**
 * Created by dlede on 07.03.2016.
 */
public class CheckTerminalConnectionRequest extends QuikRequest {

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_IS_QUIK_CONNECTED(errorCode, errorMessage, errorMessage.length);
    }
}
