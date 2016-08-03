package trade.core.model;

import lombok.*;
import trade.core.exceptions.UnsupportedDirection;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Getter
public enum Direction {

	Buy("B", 1), Sell("S", -1), Neutral("N", 0), Hold("H", 0);

	private String name;
	private int sign;

	Direction(String name, int sign) {
		this.name = name;
		this.sign = sign;
	}

	public Direction opposite() {
		if (this.equals(Buy))
			return Sell;

		if (this.equals(Sell))
			return Buy;

		return Neutral;
	}

	public boolean isOppositeTo(Direction direction) {
		if (Buy.equals(this) && Sell.equals(direction))
			return true;

		if (Sell.equals(this) && Buy.equals(direction))
			return true;

		return false;
	}

	public boolean hasSame(String name) {
		return this.name.equals(name);
	}

	public static Direction getBy(String name) throws UnsupportedDirection {
		for (Direction direction : values())
			if (direction.hasSame(name))
				return direction;

		throw new UnsupportedDirection(name);
	}

	public boolean isActive() {
		return equals(Buy) || equals(Sell);
	}
}
