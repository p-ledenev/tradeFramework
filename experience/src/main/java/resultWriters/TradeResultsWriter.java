package resultWriters;

import model.MachineStatesCollector;
import model.PortfolioStateCollector;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 03.04.2015.
 */
public class TradeResultsWriter {

    private List<ResultWriter> writers;

    public TradeResultsWriter(List<MachineStatesCollector> machineCollectors, PortfolioStateCollector portfolioCollector) {

        writers = new ArrayList<ResultWriter>();

        if (machineCollectors.size() > 1) {
            writers.add(new MachinesDataWriter(machineCollectors, "machinesMoney"));
            writers.add(new PortfolioDataWriter(portfolioCollector, "averageMoney"));

        } else {
            writers.add(new DetailMachineDataWriter(machineCollectors, "tradeResult"));
        }

        writers.add(new SummaryDataWriter(machineCollectors, portfolioCollector, "machinesSummary"));
    }

    public void write() throws Throwable {

        for (ResultWriter writer : writers)
            writer.write();
    }
}
