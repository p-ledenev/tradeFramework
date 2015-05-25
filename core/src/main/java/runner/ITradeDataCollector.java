package runner;

import model.Order;

import java.util.List;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public interface ITradeDataCollector {

    void collect(List<Order> orders);

    ITradeDataWriter getResultWriter();
}
