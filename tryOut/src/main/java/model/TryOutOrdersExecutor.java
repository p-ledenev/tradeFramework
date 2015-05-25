package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class TryOutOrdersExecutor implements IOrdersExecutor {

    private List<TryOutCandle> candles;

    public TryOutOrdersExecutor(List<TryOutCandle> candles) {
        this.candles = candles;
    }

    public void execute(List<Order> orders) throws Throwable {

        for (Order order : orders) {
            TryOutCandle next = (TryOutCandle) order.getLastCandle();
            order.setPositionValue(next.getNextValue());

            order.executed();
        }
    }
}
