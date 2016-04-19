package orders.callbacks;

import com.sun.jna.NativeLong;
import orders.dictionary.ResponseCode;
import orders.jnative.Trans2QuikLibrary;
import org.joda.time.*;
import protocols.QuikTransactionsGateway;
import tools.Log;

/**
 * Created by pledenev on 07.03.2016.
 */
public class ConnectionStatusCallback implements Trans2QuikLibrary.ConnectionStatusCallback {

	private QuikTransactionsGateway gateway;

	public ConnectionStatusCallback(QuikTransactionsGateway gateway) {
		this.gateway = gateway;
	}

	@Override
	public void callback(NativeLong eventCode, NativeLong extendedErrorCode, String errorMessage) {

		if (DateTime.now().getDayOfWeek() == DateTimeConstants.SATURDAY ||
				DateTime.now().getDayOfWeek() == DateTimeConstants.SUNDAY)
			return;

		Log.debug("ConnectionStatusCallback"
				+ " eventCode: " + ResponseCode.getBy(eventCode.longValue())
				+ "; extendedErrorCode: " + extendedErrorCode.longValue()
				+ "; errorMessage: " + errorMessage);

		gateway.setConnectionStatus(ResponseCode.getBy(eventCode.longValue()));

		if (ResponseCode.isDllDisconnected(eventCode.longValue())) {
			try {
				gateway.connect();
			} catch (Throwable e) {
				Log.error(e);
			}
		}
	}
}
