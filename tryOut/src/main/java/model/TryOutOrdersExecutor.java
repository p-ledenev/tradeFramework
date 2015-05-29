package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class TryOutOrdersExecutor implements IOrdersExecutor {

    private List<TryOutCandle> candles;
    private int tradeYear;

    public TryOutOrdersExecutor(List<TryOutCandle> candles, int tradeYear) {
        this.candles = candles;
        this.tradeYear = tradeYear;
    }

    public void execute(List<Order> orders) throws Throwable {

        for (Order order : orders) {

            Candle next = order.getLastCandle();
            if (!next.hasYearAs(tradeYear))
                continue;


            order.setPositionValue(((TryOutCandle)next).getNextValue());
            order.executed();
        }
    }
}
