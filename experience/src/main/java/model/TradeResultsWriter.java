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

    public void write() throws Throwable{

        if (collectors.size() > 1) {
            writeMachinesData();
            writePortfolioData();
        } else {
            writeDetailMachineData();
        }

        writeSummaryData();
    }

    protected void writeDetailMachineData() throws Throwable {

        String security = getPortfolio().getSecurity();
        String title = getPortfolio().getTitle();
        int year = collectors.get(0).getYear();

        PrintWriter writer = new PrintWriter(resultPath + "\\machinesMoney_" + security + "_" + year + "_" + title + ".csv", "utf-8");
        for(MachineStatesCollector collector : collectors) {
            writer.write("");
        }

        writer.close();
    }

    protected void writePortfolioData() {

    }

    protected void writeSummaryData() {
        
    }

    protected void writeMachinesData() {

    }

    protected Portfolio getPortfolio() {
        return collectors.get(0).getPortfolio();
    }
}
