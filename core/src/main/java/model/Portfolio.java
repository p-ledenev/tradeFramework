package model;

import lombok.*;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Portfolio {

    protected List<Machine> machines;
    protected String security;
    protected String title;

    public Order processCandles(List<Candle> candles) {
        //TODO
        return null;
    }
}
