package model;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public class Candle {

    protected DateTime date;
    protected double value;

    public Candle(DateTime date, double value) {
        this.date = date;
        this.value = value;
    }

    public boolean isTimeInRange(LocalTime tradeFrom, LocalTime tradeTo) {
        int dateMillisecs = date.toLocalTime().getMillisOfDay();
        return dateMillisecs >= tradeFrom.getMillisOfDay() && dateMillisecs <= tradeTo.getMillisOfDay();
    }
}
