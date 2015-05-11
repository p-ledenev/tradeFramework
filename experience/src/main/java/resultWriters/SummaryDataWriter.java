package resultWriters;

import model.MachineStatesCollector;
import model.PortfolioStateCollector;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class SummaryDataWriter extends ResultWriter {

    private List<String> summary;

    public SummaryDataWriter(List<MachineStatesCollector> machineCollectors, PortfolioStateCollector portfolioCollector, String fileName) {
        super(machineCollectors, portfolioCollector, fileName);

        summary = new ArrayList<String>() {
            {
                add(" |");
                add("maxLosses|");
                add("maxMoneys|");
                add("endPeriodMoneys|");
            }
        };
    }

    @Override
    public void writeTo(PrintWriter writer) {

        for (MachineStatesCollector collector : machineCollectors) {
            addTo(0, collector.getDepth());
            addTo(1, collector.computeMaxLosses());
            addTo(2, collector.computeMaxRelativeMoney());
            addTo(3, collector.computeEndPeriodRelativeMoney());
        }

        for (String result : summary)
            writer.write(result);
    }

    private void addTo(int index, double data) {
        summary.set(index, summary.get(index) + data + "|");
    }
}