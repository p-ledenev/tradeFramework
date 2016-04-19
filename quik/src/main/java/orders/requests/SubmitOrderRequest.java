package orders.requests;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.model.Transaction;
import tools.Log;

/**
 * Created by pledenev on 07.03.2016.
 */

@AllArgsConstructor
public class SubmitOrderRequest extends QuikRequest {

    private Transaction transaction;

    @Override
    protected NativeLong executeNativeRequest() throws Throwable {

		Log.info("Transaction submitted " + transaction.buildQuikString());

        return library.TRANS2QUIK_SEND_ASYNC_TRANSACTION(
                transaction.buildQuikString(),
                errorCode,
                errorMessage,
                errorMessage.length
        );
    }
}
