package protocols.orders.requests;

import com.sun.jna.NativeLong;

/**
 * Created by dlede on 07.03.2016.
 */
public class ConnectDllToTerminalRequest extends QuikRequest {

    private String pathToQuik;

    public ConnectDllToTerminalRequest(String pathToQuik) {
        this.pathToQuik = pathToQuik;
    }

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_CONNECT(pathToQuik, errorCode, errorMessage, errorMessage.length);
    }
}
