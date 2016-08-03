package trade.terminals.quik.candles;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import trade.core.model.Candle;

import java.util.*;

/**
 * Created by ledenev.p on 17.03.2016.
 */
public class CandleParser {

	public static List<Candle> asList(List<String> lines) throws Throwable {

		List<Candle> response = new ArrayList<>();
		CandleParser parser = new CandleParser();
		for (String strCandle : lines)
			response.add(0, parser.parse(strCandle));

		return response;
	}

	public Candle parse(String strCandle) throws Throwable {
		String[] params = strCandle.split(";");

		DateTime date = parseDate(params[0]);
		double value = Double.parseDouble(params[1]);

		return new Candle(date, value);
	}

	private DateTime parseDate(String strDate) throws Throwable {
		return DateTime.parse(strDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
