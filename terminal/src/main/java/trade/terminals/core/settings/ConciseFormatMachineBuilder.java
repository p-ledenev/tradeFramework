package trade.terminals.core.settings;

import lombok.*;
import org.joda.time.DateTime;
import trade.core.decisionStrategies.DecisionStrategy;
import trade.core.model.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */

@Getter @Setter
public class ConciseFormatMachineBuilder {

	private int depth;
	private boolean isBlocked;
	private Direction direction;

	@Getter
	private Machine machine;

	public ConciseFormatMachineBuilder(String line) throws Throwable {
		String[] params = line.split(";");

		depth = Integer.parseInt(params[0]);
		isBlocked = params[2].equals("1") ? true : false;
		direction = Direction.getBy(params[1]);
	}

	public ConciseFormatMachineBuilder(Machine machine) {
		this.machine = machine;
	}

	public void build(Portfolio portfolio, DecisionStrategy decisionStrategy, double commission) {

		machine = Machine.with(portfolio, decisionStrategy, commission, depth);

		Candle candle = Candle.empty(new DateTime());

		int volume = direction.isActive() ? 1 : 0;
		Position position = Position.opening(direction, volume, candle);

		machine.setPosition(position);
	}

	public String serialize() {
		return machine.getDepth() + ";" + machine.getPositionDirection().getName() + ";" +
				(machine.isBlocked() ? "1" : "0");
	}
}
