package settings;

import decisionStrategies.*;
import lombok.*;
import model.*;
import org.joda.time.*;

/**
 * Created by ledenev.p on 19.06.2015.
 */

@Data
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
        Position position = Position.opening(direction, 1, candle);

        machine.setPosition(position);
    }

    public String serialize() {
        return machine.getDepth() + ";" + machine.getPositionDirection().getName() + ";" +
                (machine.isBlocked() ? "1" : "0");
    }
}