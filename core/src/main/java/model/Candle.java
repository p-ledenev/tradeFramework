package model;

import lombok.Data;
import org.joda.time.*;
import tools.*;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
public class Candle implements Cloneable {

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

	public String print() {
		return "date: " + Format.asString(date) + "; value: " + Round.toMoneyAmount(value);
	}

	public double computeVariance(double value) {
		return Math.abs(this.value - value) / value * 100;
	}

	public double computeVariance(Candle candle) {
		return this.computeVariance(candle.getValue());
	}

	public boolean hasValueGreaterThan(double value) {
		return this.value > value;
	}

	public boolean hasValueLessThan(double value) {
		return this.value < value;
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

	public boolean hasSameDate(Candle candle) {
		return getDate().equals(candle.getDate());
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

	public boolean hasDate(DateTime date) {
		return this.date.equals(date);
	}

	public boolean before(Candle candle) {
		return date.isBefore(candle.getDate());
	}

	public boolean hasTimeGreaterThan(LocalTime time) {

		return date.toLocalTime().compareTo(time) > 0;
	}

	public boolean hasDateGreaterThan(DateTime date) {
		return this.date.compareTo(date) > 0;
	}

	public boolean hasDateLessThan(DateTime date) {
		return this.date.compareTo(date) < 0;
	}

	public boolean hasLastTimeWithIn(LocalTime from, LocalTime to) {
		LocalTime current = date.toLocalTime();
		return current.compareTo(from) >= 0 && current.compareTo(to) <= 0;
	}
}
