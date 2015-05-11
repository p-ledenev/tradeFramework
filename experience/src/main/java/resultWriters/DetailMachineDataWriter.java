package resultWriters;

import model.MachineStatesCollector;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class DetailMachineDataWriter extends ResultWriter {

    public DetailMachineDataWriter(List<MachineStatesCollector> machineCollectors, String fileName) {
        super(machineCollectors, null, fileName);
    }

    @Override
    public void writeTo(PrintWriter writer) {

    }
}
