package trade.core.model;

import lombok.*;
import org.joda.time.*;

/**
 * Created by DiKey on 04.04.2015.
 */

@Getter @Setter
@AllArgsConstructor
public abstract class Position implements Cloneable {

	private Candle candle;

	public static Position begining() {
		return new NeutralPosition(Candle.empty());
	}

	public static Position closing(Candle candle) {
		return new NeutralPosition(candle);
	}

	public static Position opening(Direction direction, int volume, Candle candle) {
		return new ActivePosition(direction, volume, candle);
	}

	public double computeProfit(double value) {
		return getDirection().getSign() * getVolume() * (value - getValue());
	}

	public boolean hasSameDirectionAs(Position position) {
		return getDirection().equals(position.getDirection());
	}

	public boolean hasSameDay(Position position) {
		return candle.getDate().toLocalDate().equals(position.getDate().toLocalDate());
	}

	public DateTime getDate() {
		return candle.getDate();
	}

	public double getValue() {
		return candle.getValue();
	}

	public int getYear() {
		return getDate().getYear();
	}

	public boolean isBuy() {
		return Direction.Buy.equals(getDirection());
	}

	public boolean isSell() {
		return Direction.Sell.equals(getDirection());
	}

	public boolean isHold() {
		return Direction.Hold.equals(getDirection());
	}

	public boolean isNeutral() {
		return Direction.Neutral.equals(getDirection());
	}

	public boolean equalTo(Position position) {
		if (position == null)
			return false;

		if (!this.getClass().equals(position.getClass()))
			return false;

		return hasEqualParams(position);
	}

	public void setValue(double value) {
		candle.setValue(value);
	}

	public void setDate(DateTime date) {
		candle.setDate(date);
	}

	public int getSignVolume() {
		return getDirection().getSign() * getVolume();
	}

	public String printCSV() {
		return candle.printTitleCSV();
	}

	public abstract Direction getDirection();

	public abstract int getVolume();

	protected abstract boolean hasEqualParams(Position position);
}
