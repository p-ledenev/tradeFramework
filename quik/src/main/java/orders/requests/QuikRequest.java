package orders.requests;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import orders.model.*;
import orders.jnative.*;

/**
 * Created by dlede on 07.03.2016.
 */
public abstract class QuikRequest {

    protected NativeLongByReference errorCode;
    protected byte[] errorMessage;

    protected Trans2QuikLibrary library;

    public QuikRequest() {
        errorCode = new NativeLongByReference();
        errorMessage = new byte[1024];

        library = Trans2QuikLibraryLoader.library;
    }

    public QuikResponse execute() throws Throwable {
        NativeLong resultCode = executeNativeRequest();

        return QuikResponse.create(resultCode, errorCode, errorMessage);
    }

    protected abstract NativeLong executeNativeRequest() throws Throwable;
}
