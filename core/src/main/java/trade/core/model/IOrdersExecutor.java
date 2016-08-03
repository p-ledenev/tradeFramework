package trade.core.model;

import java.util.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public interface IOrdersExecutor {

    void execute(List<Order> orders) throws Throwable;

    int loadVolumeFor(String security) throws Throwable;
}
