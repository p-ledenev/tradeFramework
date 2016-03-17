package candles.model;

import model.Candle;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.*;

/**
 * Created by ledenev.p on 17.03.2016.
 */
public class CandleParser {

	public static List<Candle> asList(String data) throws Throwable {

		String[] strCandles = data.split("\n");
		List<Candle> response = new ArrayList<>();

		CandleParser parser = new CandleParser();
		for (String strCandle : strCandles)
			response.add(parser.parse(strCandle));

		return response;
	}

	public Candle parse(String strCandle) throws Throwable {
		String[] params = strCandle.split(";");

		DateTime date = parseDate(params[0], params[1]);
		double value = Double.parseDouble(params[5]);

		return new Candle(date, value);
	}

	private DateTime parseDate(String strDate, String strTime) throws Throwable {
		return DateTime.parse(strDate + " " + strTime,
				DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss"));
	}
}
