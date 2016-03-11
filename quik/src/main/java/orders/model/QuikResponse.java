package orders.model;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.*;
import orders.dictionary.ReturnCodes;

/**
 * Created by dlede on 05.03.2016.
 */

@Data
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
		return ReturnCodes.TRANS2QUIK_SUCCESS.equals(returnCode);
	}

	public boolean isQuikConnected() {
		return ReturnCodes.TRANS2QUIK_QUIK_CONNECTED.equals(returnCode) || isSuccess();
	}

	public boolean isDllConnected() {
		return ReturnCodes.TRANS2QUIK_DLL_CONNECTED.equals(returnCode) || isSuccess();
	}
}
