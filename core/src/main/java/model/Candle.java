package model;

import approximationConstructors.IApproximationSupport;
import averageConstructors.IAveragingSupport;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import tools.Format;
import tools.Log;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
public class Candle implements IAveragingSupport, IApproximationSupport, Cloneable {

    public static int relevancePeriodMinutes = 1;

    protected DateTime date;
    protected double value;

    public static Candle empty() {
        return new Candle(new DateTime(0), 0);
    }

    public static Candle empty(DateTime date) {
        return new Candle(new DateTime(date.getMillis()), 0);
    }

    public Candle(DateTime date, double value) {
        this.date = date;
        this.value = value;
    }

    public boolean isTimeInRange(LocalTime tradeFrom, LocalTime tradeTo) {
        int dateMillisecs = date.toLocalTime().getMillisOfDay();
        return dateMillisecs >= tradeFrom.getMillisOfDay() && dateMillisecs <= tradeTo.getMillisOfDay();
    }

    public boolean isRelevant() {
        long dateMillisecs = date.getMillis();
        long currentMillisecs = new DateTime().getMillis();

        return (currentMillisecs - dateMillisecs) < relevancePeriodMinutes * 60 * 1000;
    }

    public String print() {
        return "date: " + Format.asString(date) + "; value: " + value;
    }

    public double computeVariance(double value) {
        return Math.abs(this.value - value) / value * 100;
    }

    public boolean greaterThan(double value) {
        return this.value > value;
    }

    public boolean lessThan(double value) {
        return this.value < value;
    }

    @Override
    public double getValueForApproximation() {
        return value;
    }

    @Override
    public double getValueForAveraging() {
        return value;
    }

    public String printCSV() {
        return printTitleCSV() + ";" + value;
    }

    protected String printTitleCSV() {
        return Format.indexFor(date) + ";" + Format.asString(date);
    }

    public boolean hasSameDay(Candle candle) {
        return getDateDay() == candle.getDateDay();
    }

    public int getDateDay() {
        return date.getDayOfYear();
    }

    public boolean hasYearAs(int year) {
        return date.getYear() == year;
    }

    public boolean equals(Candle candle) {
        return date.equals(candle.getDate()) && value == value;
    }

    @Override
    public Candle clone() {
        try {
            return (Candle) super.clone();

        } catch (CloneNotSupportedException e) {
            Log.error("Clone for candle " + print() + " exception", e);
        }

        return null;
    }
}
