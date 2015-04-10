package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Portfolio {

    private List<Machine> machines;
    private String security;
    private String title;

    public List<Order> processCandles(List<Candle> candles) {

        List<Order> orders = new ArrayList<Order>();
        for (Machine machine : machines)
            orders.add(machine.processCandles(candles));

        return orders;
    }

    public String printStrategy() {
        // TODO
        return null;
    }
}
