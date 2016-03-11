package orders.callbacks;

import com.sun.jna.NativeLong;
import lombok.AllArgsConstructor;
import orders.model.Trans2QuikLibrary;
import protocols.QuikOrdersGateway;

/**
 * Created by dlede on 07.03.2016.
 */

@AllArgsConstructor
public class OrderStatusCallback implements Trans2QuikLibrary.OrderStatusCallback {

	private QuikOrdersGateway gateway;

	@Override
	public void callback(NativeLong mode,
						 int transactionId,
						 double orderNumber,
						 String classCode,
						 String securityCode,
						 double value,
						 NativeLong restVolume,
						 double volume,
						 NativeLong isSell,
						 NativeLong status,
						 NativeLong nOrderDescriptor) {

	}
}
