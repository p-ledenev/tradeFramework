package model;

import org.joda.time.*;

import java.util.*;

/**
 * Created by ledenev.p on 10.06.2015.
 */
public interface ICandlesIterator {

    List<Candle> getNextCandlesFor(String security, DateTime dateFrom, DateTime dateTo) throws Throwable;
}
