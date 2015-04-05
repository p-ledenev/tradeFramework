package model;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by ledenev.p on 03.04.2015.
 */
public class TradeResultsWriter {

    public static String resultPath = "results";

    protected List<MachineStatesCollector> collectors;

    public TradeResultsWriter(Portfolio portfolio, List<MachineStatesCollector> collectors) {
        this.collectors = collectors;
    }

    public void write() throws Throwable {

        if (collectors.size() > 1) {
            writeMachinesData();
            writePortfolioData();
        } else {
            writeDetailMachineData();
        }

        writeSummaryData();
    }

    protected void writeDetailMachineData() throws Throwable {
        //TODO
    }

    protected void writePortfolioData() throws Throwable {
        //TODO
    }

    protected void writeSummaryData() throws Throwable {
        //TODO
    }

    protected void writeMachinesData() throws Throwable {

        String security = getPortfolio().getSecurity();
        String title = getPortfolio().getTitle();
        int year = collectors.get(0).getYear();

        PrintWriter writer = new PrintWriter(resultPath + "\\machinesMoney_" + security + "_" + year + "_" + title + ".csv", "utf-8");

        String head = "";
        for (MachineStatesCollector collector : collectors)
            head += collector.printMachineHead() + "; ;";
        writer.write(head);

        int maxCapacity = getCollectorsMaxCapacity();
        for (int i = 0; i < maxCapacity; i++) {
            String info = "";
            for (MachineStatesCollector collector : collectors)
                info += collector.printState(i) + "; ;";

            writer.write(info);
        }

        writer.close();

    }

    protected Portfolio getPortfolio() {
        return collectors.get(0).getPortfolio();
    }

    protected int getCollectorsMaxCapacity() {

        int maxCapacity = 0;
        for (MachineStatesCollector collector : collectors)
            if (collector.getStatesSize() > maxCapacity)
                maxCapacity = collector.getStatesSize();

        return maxCapacity;
    }
}
