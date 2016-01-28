package model;

import lombok.*;
import org.joda.time.*;
import tools.*;

/**
 * Created by ledenev.p on 21.05.2015.
 */

@Data
public class TryOutCandle extends Candle {

    private double nextValue;
    private int index;

    public static TryOutCandle with(double value, double nextValue, int index, DateTime date) throws Throwable {

        TryOutCandle candle = new TryOutCandle(date, value);
        candle.setIndex(index);
        candle.setNextValue(nextValue);

        return candle;
    }

    public TryOutCandle(DateTime date, double value) {
        super(date, value);
    }

    @Override
    protected String printTitleCSV() {
        return index + ";" + Format.asString(date);
    }
}
