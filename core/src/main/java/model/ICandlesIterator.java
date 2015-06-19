package model;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 10.06.2015.
 */
public interface ICandlesIterator {

    List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable;
}
