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
public class FullFormatMachineBuilder {

    private int depth;
    private double currentMoney;
    private boolean isBlocked;
    private DateTime date;
    private Direction direction;
    private double value;
    private int volume;

    @Getter
    private Machine machine;

    public FullFormatMachineBuilder(String line) throws Throwable {
        String[] params = line.split("\\t");

        depth = Integer.parseInt(params[0]);
        currentMoney = Double.parseDouble(params[1]);
        isBlocked = params[2].equals("1") ? true : false;
        date = Format.asDate(params[3]);
        direction = Direction.getBy(params[4]);
        value = Double.parseDouble(params[5]);
        volume = Integer.parseInt(params[6]);
    }

    public FullFormatMachineBuilder(Machine machine) {
        this.machine = machine;
    }

    public void build() {

        Candle candle = new Candle(date, value);
        Position position = Position.opening(direction, volume, candle);

        machine = new Machine(position, depth, currentMoney, isBlocked);
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
