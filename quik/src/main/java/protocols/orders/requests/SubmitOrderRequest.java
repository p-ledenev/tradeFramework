package protocols.orders.requests;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import protocols.orders.model.QuikTransaction;

/**
 * Created by pledenev on 07.03.2016.
 */

@AllArgsConstructor
public class SubmitOrderRequest extends QuikRequest {

    private QuikTransaction transaction;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {
        return library.TRANS2QUIK_SEND_ASYNC_TRANSACTION(
                transaction.buildQuikString(),
                errorCode,
                errorMessage,
                errorMessage.length
        );
    }
}
