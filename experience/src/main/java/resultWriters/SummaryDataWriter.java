package resultWriters;

import model.MachineMoneyStatesCollector;
import model.Portfolio;
import model.PortfolioMoneyStatesCollector;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class SummaryDataWriter extends ResultWriter {

    private List<MachineMoneyStatesCollector> machineCollectors;
    private PortfolioMoneyStatesCollector portfolioCollector;
    private List<String> summary;

    public SummaryDataWriter(List<MachineMoneyStatesCollector> machineCollectors, PortfolioMoneyStatesCollector portfolioCollector, String fileName) {
        super(fileName);

        this.machineCollectors = machineCollectors;
        this.portfolioCollector = portfolioCollector;

        summary = new ArrayList<String>() {
            {
                add(" ;");
                add("maxLosses;");
                add("maxMoneys;");
                add("endPeriodMoneys;");
            }
        };
    }

    @Override
    public void writeTo(PrintWriter writer) {

        for (MachineMoneyStatesCollector collector : machineCollectors) {
            addTo(0, collector.getDepth());
            addTo(1, collector.computeMaxLosses());
            addTo(2, collector.computeMaxRelativeMoney());
            addTo(3, collector.computeEndPeriodRelativeMoney());
        }

        addTo(0, "average");
        addTo(1, portfolioCollector.computeMaxLosses());
        addTo(2, portfolioCollector.computeMaxRelativeMoney());
        addTo(3, portfolioCollector.computeEndPeriodRelativeMoney());

        for (String result : summary)
            writer.write(result);
    }

    @Override
    protected Portfolio getPortfolio() {
        return portfolioCollector.getEntity();
    }

    @Override
    protected int getYear() {
        return portfolioCollector.getYear();
    }

    private void addTo(int index, String data) {
        summary.set(index, summary.get(index) + data + ";");
    }

    private void addTo(int index, double data) {
        summary.set(index, summary.get(index) + data + ";");
    }
}