package protocols;

import model.*;

import java.util.List;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersExecutor implements IOrdersExecutor {

    private QuikOrdersGateway gateway;

    @Override
    public void execute(List<Order> orders) throws InterruptedException {

    }

    @Override
    public void checkVolumeFor(String security, int volume) throws Throwable {

    }
}
