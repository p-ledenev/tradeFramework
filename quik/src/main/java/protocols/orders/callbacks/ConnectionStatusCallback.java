package protocols.orders.callbacks;

import com.sun.jna.NativeLong;
import protocols.orders.Trans2QuikLibrary;

/**
 * Created by dlede on 07.03.2016.
 */
public class ConnectionStatusCallback implements Trans2QuikLibrary.ConnectionStatusCallback {

    @Override
    public void callback(NativeLong eventCode, NativeLong extendedErrorCode, String errorMessage) {

    }
}
