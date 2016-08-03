package trade.terminals.quik.orders.model;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.*;
import trade.terminals.quik.orders.dictionary.ResponseCode;

/**
 * Created by dlede on 05.03.2016.
 */

@Getter @Setter
@NoArgsConstructor
public class QuikResponse {

	public static QuikResponse create(NativeLong returnCode, NativeLongByReference errorCode, byte[] errorMessage) {
		QuikResponse response = new QuikResponse();

		response.setReturnCode(returnCode.longValue());
		response.setErrorCode(errorCode.getValue().longValue());
		response.setErrorMessage(new String(errorMessage));

		return response;
	}

	Long errorCode;
	Long returnCode;
	String errorMessage;

	public boolean isSuccess() {
		return ResponseCode.isSucceed(returnCode);
	}

	public boolean isQuikConnected() {
		return ResponseCode.isQuikConnected(returnCode) || isSuccess();
	}

	public boolean isDllConnected() {
		return ResponseCode.isDlLConnected(returnCode) || isSuccess();
	}
}
