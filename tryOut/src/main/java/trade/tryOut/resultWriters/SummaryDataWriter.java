package trade.tryOut.resultWriters;

import trade.core.model.Portfolio;
import trade.tryOut.model.*;

import java.io.PrintWriter;
import java.util.*;

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
                add(portfolioCollector.getYear() + ";");
                add("maxLossesPercent;");
                add("maxMoneyPercent;");
                add("endPeriodMoneyPercent;");
            }
        };
    }

    @Override
    public void writeTo(PrintWriter writer) {

        for (MachineMoneyStatesCollector collector : machineCollectors) {
            addTo(0, Integer.toString(collector.getDepth()));
            addTo(1, collector.computeMaxLossesPercent());
            addTo(2, collector.computeMaxMoneyPercent());
            addTo(3, collector.computeEndPeriodMoneyPercent());
        }

        addTo(0, "average");
        addTo(1, portfolioCollector.computeMaxLossesPercent());
        addTo(2, portfolioCollector.computeMaxMoneyPercent());
        addTo(3, portfolioCollector.computeEndPeriodMoneyPercent());

        for (String result : summary)
            writer.write(result + "\n");
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