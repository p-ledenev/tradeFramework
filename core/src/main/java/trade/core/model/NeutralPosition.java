package trade.core.model;

/**
 * Created by ledenev.p on 19.04.2016.
 */
public class NeutralPosition extends Position {

	public NeutralPosition(Candle candle) {
		super(candle);
	}

	@Override
	public Direction getDirection() {
		return Direction.Neutral;
	}

	@Override
	public int getVolume() {
		return 0;
	}

	@Override
	public boolean hasEqualParams(Position position) {
		return true;
	}
}
