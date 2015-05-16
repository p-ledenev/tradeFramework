package model;

import java.util.List;

/**
 * Created by DiKey on 16.05.2015.
 */
public interface ICandlesProcessor {

    List<Order> processNext(Candle... candles) throws Throwable;
}
