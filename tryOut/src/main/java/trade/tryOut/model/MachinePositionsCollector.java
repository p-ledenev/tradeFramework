package trade.tryOut.model;

import lombok.*;
import trade.core.model.*;

import java.util.*;

/**
 * Created by ledenev.p on 12.05.2015.
 */

@Getter @Setter
public class MachinePositionsCollector {

    private List<Position> positions;
    private Machine machine;

    public MachinePositionsCollector(Machine machine) {
        this.machine = machine;

        positions = new ArrayList<Position>();
    }

    public Portfolio getPortfolio() {
        return machine.getPortfolio();
    }

    public int getYear() {
        return getLastPosition().getYear();
    }

    public Position getLastPosition() {
        return get(positions.size() - 1);
    }

    public Position get(int i) {
        if (positions.size() <= i || i < 0)
            return null;

        return positions.get(i);
    }

    public void addPositionIfChanged() {

        if (machine.getPosition().equalTo(getLastPosition()))
            return;

        positions.add(machine.getPosition());
    }

    public boolean hasPosition(int i) {
        return get(i) != null;
    }
}
