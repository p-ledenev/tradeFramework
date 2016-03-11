package orders.callbacks;

import com.sun.jna.NativeLong;
import orders.dictionary.ReturnCodes;
import orders.model.Trans2QuikLibrary;
import protocols.QuikOrdersGateway;
import tools.Log;

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
		Log.info("Dll connection status callback received with event code " + eventCode);

		if (ReturnCodes.isDllDisconnected(eventCode.longValue())) {
			try {
				gateway.connect();
			} catch (Throwable e) {
				Log.error("", e);
			}
		}
	}
}
