package model;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
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

    public String print() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

        return "date: " + formatter.print(date) + "; value: " + value;
    }
}
