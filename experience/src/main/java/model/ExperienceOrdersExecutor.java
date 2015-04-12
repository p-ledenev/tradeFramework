package model;

import exceptions.CandleNotFoundFailure;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class ExperienceOrdersExecutor implements IOrdersExecutor {

    private List<Candle> candles;

    public ExperienceOrdersExecutor(List<Candle> candles) {
        this.candles = candles;
    }

    public void execute(List<Order> orders) throws Throwable {

        for (Order order : orders) {
            Candle candle = order.getLastCandle();
            Candle next = findCandleAfter(candle);

            order.setPositionDate(candle.getDate());
            order.setPositionValue(next.getValue());
            order.executed();
        }
    }

    private Candle findCandleAfter(Candle candle) throws CandleNotFoundFailure {
        int index = candles.indexOf(candle);

        if (index < 0)
            throw new CandleNotFoundFailure(candle.print());

        if (index != candles.size() - 1)
            index++;

        return candles.get(index);
    }
}
