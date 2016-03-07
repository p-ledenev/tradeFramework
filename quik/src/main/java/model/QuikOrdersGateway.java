package model;

import lombok.Setter;
import protocols.orders.*;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersGateway {

    @Setter
    private String pathToQuik;
    private Trans2QuikAdapter adapter;

    public void dropOrder(int orderId) {
        adapter = new Trans2QuikAdapter();
    }

    public String loadStatus(int orderId) {
        return null;
    }

    public int submit(String security, String direction, int volume, double value) {
        return 0;
    }

    public int loadSecurityVolume(String security) {
        return 0;
    }

    private void connect() throws Throwable {
        QuikResponse response = adapter.connect(pathToQuik);
    }
}
