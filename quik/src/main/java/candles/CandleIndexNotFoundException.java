package candles;

import org.joda.time.DateTime;
import tools.Format;

/**
 * Created by ledenev.p on 24.03.2016.
 */
public class CandleIndexNotFoundException extends Exception {

	public CandleIndexNotFoundException(DateTime date) {
		super("Can't find candle in list for date " + Format.asString(date));
	}
}