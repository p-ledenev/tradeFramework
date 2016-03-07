package protocols.orders;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;
import lombok.*;

/**
 * Created by dlede on 05.03.2016.
 */

@Data
@NoArgsConstructor
public class QuikResponse {

    public static QuikResponse create() {
        return new QuikResponse();
    }

    public static QuikResponse create(NativeLong returnCode, NativeLongByReference errorCode, byte[] errorMessage) {
        QuikResponse response = create();

        response.setReturnCode(returnCode.longValue());
        response.setErrorCode(errorCode.getValue().longValue());
        response.setErrorMessage(new String(errorMessage));

        return response;
    }

    public static QuikResponse createWith(Long returnCode) {
        QuikResponse response = create();
        response.setReturnCode(returnCode);

        return response;
    }

    Long errorCode;
    Long returnCode;
    String errorMessage;
}
