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

    public TryOutCandle(DateTime date, double value) {
        super(date, value);
    }

    @Override
    public String printTitleCSVFor() {
        return index + ";" + Format.asString(date);
    }
}
