package trade.core.model;

import lombok.*;

/**
 * Created by ledenev.p on 19.04.2016.
 */

@Getter @Setter
public class ActivePosition extends Position {

	private Direction direction;
	private int volume;

	public ActivePosition(Direction direction, int volume, Candle candle) {
		super(candle);

		this.direction = direction;
		this.volume = volume;
	}

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public int getVolume() {
		return volume;
	}

	@Override
	public boolean hasEqualParams(Position position) {
		return getDirection().equals(position.getDirection()) &&
				getVolume() == position.getVolume() &&
				getValue() == position.getValue();
	}
}
