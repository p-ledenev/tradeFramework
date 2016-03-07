package protocols.orders.requests;

import com.sun.jna.*;
import lombok.AllArgsConstructor;
import protocols.orders.Trans2QuikLibrary.ConnectionStatusCallback;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class RegisterConnectionStatusCallbackRequest extends QuikRequest {

    private ConnectionStatusCallback connectionStatusCallback;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SET_CONNECTION_STATUS_CALLBACK(connectionStatusCallback, errorCode, errorMessage, errorMessage.length);
    }
}
