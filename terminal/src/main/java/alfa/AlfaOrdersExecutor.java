package alfa;

import model.IOrdersExecutor;
import model.Order;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class AlfaOrdersExecutor implements IOrdersExecutor {

    private AlfaGateway gateway;

    public AlfaOrdersExecutor(AlfaGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(List<Order> orders) {
        // TODO real market execution
    }
}
