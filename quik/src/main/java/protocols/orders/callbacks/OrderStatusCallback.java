package protocols.orders.callbacks;

import com.sun.jna.NativeLong;
import protocols.orders.Trans2QuikLibrary;

/**
 * Created by dlede on 07.03.2016.
 */
public class OrderStatusCallback implements Trans2QuikLibrary.OrderStatusCallback {

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
