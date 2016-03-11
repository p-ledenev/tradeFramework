package orders.callbacks;

import com.sun.jna.NativeLong;
import orders.model.Trans2QuikLibrary;
import protocols.QuikOrdersGateway;

/**
 * Created by dlede on 07.03.2016.
 */
public class ConnectionStatusCallback implements Trans2QuikLibrary.ConnectionStatusCallback {

	private QuikOrdersGateway gateway;

	public ConnectionStatusCallback(QuikOrdersGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public void callback(NativeLong eventCode, NativeLong extendedErrorCode, String errorMessage) {

	}
}
