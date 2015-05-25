package model;

import lombok.Data;
import org.joda.time.DateTime;

/**
 * Created by ledenev.p on 21.05.2015.
 */

@Data
public class TryOutCandle extends Candle {

    private double nextValue;

    public TryOutCandle(DateTime date, double value) {
        super(date, value);
    }

    public TryOutCandle(Candle candle, double nextValue) {
        this(candle.getDate(), candle.getValue());

        this.nextValue = nextValue;
    }
}
