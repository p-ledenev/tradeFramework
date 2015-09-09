package settings;

import commissionStrategies.*;
import decisionStrategies.*;
import lombok.*;
import model.*;
import org.joda.time.*;
import tools.*;

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

    public void build() {

        Candle candle = Candle.empty(new DateTime());
        Position position = Position.opening(direction, 1, candle);

        machine = new Machine(position, depth, 0, isBlocked);
    }

    public void init(Portfolio portfolio, DecisionStrategy decisionStrategy, ICommissionStrategy commissionStrategy) {
        machine.setPortfolio(portfolio);
        machine.setDecisionStrategy(decisionStrategy);
        machine.setCommissionStrategy(commissionStrategy);
    }

    public String serialize() {
        return machine.getDepth() + "\t" + Round.toDecadeAmount(machine.getCurrentMoney()) + "\t" +
                (machine.isBlocked() ? "1" : "0") + "\t" + Format.asString(machine.getPositionDate()) + "\t" +
                machine.getPositionDirection().getName() + "\t"
                + Round.toDecadeAmount(machine.getPositionValue()) + "\t" + machine.getPositionVolume();
    }
}
