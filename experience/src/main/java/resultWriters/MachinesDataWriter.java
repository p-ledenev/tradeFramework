package resultWriters;

import model.MachineStatesCollector;
import model.PortfolioStateCollector;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class MachinesDataWriter extends ResultWriter {

    public MachinesDataWriter(List<MachineStatesCollector> machineCollectors, String fileName) {
        super(machineCollectors, null, fileName);
    }

    @Override
    public void writeTo(PrintWriter writer) {

        String head = "";
        for (MachineStatesCollector collector : machineCollectors)
            head += collector.printHead() + "; ;";
        writer.write(head);

        int maxCapacity = getCollectorsMaxCapacity();
        for (int i = 0; i < maxCapacity; i++) {
            String info = "";
            for (MachineStatesCollector collector : machineCollectors)
                info += collector.printState(i) + "; ;";

            writer.write(info);
        }
    }

    private int getCollectorsMaxCapacity() {

        int maxCapacity = 0;
        for (MachineStatesCollector collector : machineCollectors)
            if (collector.getStatesSize() > maxCapacity)
                maxCapacity = collector.getStatesSize();

        return maxCapacity;
    }
}
