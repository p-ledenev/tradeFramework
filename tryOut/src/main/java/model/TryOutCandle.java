package model;

import lombok.Data;
import org.joda.time.DateTime;
import tools.Format;

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
    public String printTitleCSV() {
        return index + ";" + Format.asString(date);
    }
}
