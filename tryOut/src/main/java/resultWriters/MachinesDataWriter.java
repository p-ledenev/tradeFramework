package resultWriters;

import model.MachineMoneyStatesCollector;
import model.Portfolio;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class MachinesDataWriter extends ResultWriter {

    private List<MachineMoneyStatesCollector> statesCollectors;

    public MachinesDataWriter(List<MachineMoneyStatesCollector> statesCollectors, String fileName) {
        super(fileName);

        this.statesCollectors = statesCollectors;
    }

    @Override
    public void writeTo(PrintWriter writer) {

        String head = "";
        for (MachineMoneyStatesCollector collector : statesCollectors)
            head += collector.printHead() + "; ;";
        writer.write(head + "\n");

        int maxCapacity = getCollectorsMaxCapacity();
        for (int i = 0; i < maxCapacity; i++) {
            String info = "";
            for (MachineMoneyStatesCollector collector : statesCollectors)
                info += collector.printState(i) + "; ;";

            writer.write(info + "\n");
        }
    }

    @Override
    protected Portfolio getPortfolio() {
        return statesCollectors.get(0).getPortfolio();
    }

    @Override
    protected int getYear() {
        return statesCollectors.get(0).getYear();
    }

    private int getCollectorsMaxCapacity() {

        int maxCapacity = 0;
        for (MachineMoneyStatesCollector collector : statesCollectors)
            if (collector.getStatesSize() > maxCapacity)
                maxCapacity = collector.getStatesSize();

        return maxCapacity;
    }
}
