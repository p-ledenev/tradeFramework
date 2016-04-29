package tools;

import model.Candle;
import org.joda.time.*;
import org.joda.time.format.*;

import java.util.*;

public class Format {

	public static int indexFor(DateTime date) {

		DateTime year = new DateTime(date.getYear(), 1, 1, 0, 0);
		int days = getWorkingDaysBetweenTwoDates(year, date);

		int minutesOfDay = date.getMillisOfDay() / (1000 * 60);

		return days * (14 * 60 - 10) + minutesOfDay;
	}

	public static String asString(DateTime date) {
		return asString(date, "dd.MM.yyyy HH:mm:ss");
	}

	public static String asString(DateTime date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		return formatter.print(date);
	}

	public static DateTime asDate(String date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		return formatter.parseDateTime(date);
	}

	public static DateTime asDate(String date) {
		return asDate(date, "dd.MM.yyyy HH:mm:ss");
	}

	public static int getWorkingDaysBetweenTwoDates(DateTime startDate, DateTime endDate) {

		if (startDate.getMillis() >= endDate.getMillis())
			return 0;

		int workDays = 0;
		DateTime current = startDate;
		while (current.getMillis() < endDate.getMillis()) {
			current = current.plusDays(1);

			if (current.getDayOfWeek() != Calendar.SATURDAY && current.getDayOfWeek() != Calendar.SUNDAY)
				workDays++;
		}

		return workDays;
	}

	public static double[] toDoubleArray(Candle[] candles) {
		double[] result = new double[candles.length];
		for (int i = 0; i < candles.length; i++)
			result[i] = candles[i].getValue();

		return result;
	}

	public static double[] toDoubleArray(List<Candle> candles) {
		return toDoubleArray(candles.toArray(new Candle[candles.size()]));
	}
}
