package resultWriters;

import lombok.Data;
import model.MachineStatesCollector;
import model.Portfolio;
import model.PortfolioStateCollector;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */

@Data
public abstract class ResultWriter {

    public static String resultPath = "results";

    protected List<MachineStatesCollector> machineCollectors;
    protected PortfolioStateCollector portfolioCollector;
    private String fileName;


    public ResultWriter(List<MachineStatesCollector> machineCollectors, PortfolioStateCollector portfolioCollector, String fileName) {
        this.machineCollectors = machineCollectors;
        this.portfolioCollector = portfolioCollector;
        this.fileName = fileName;
    }

    public void write() throws Throwable {

        PrintWriter writer = createPrintWriterFor(fileName);

        writeTo(writer);

        writer.close();
    }

    public abstract void writeTo(PrintWriter writer);

    private PrintWriter createPrintWriterFor(String fileName) throws Throwable {

        String security = getPortfolio().getSecurity();
        String title = getPortfolio().getTitle();
        int year = machineCollectors.get(0).getYear();

        return new PrintWriter(resultPath + "\\" + fileName + "_" + security + "_" + year + "_" + title + ".csv", "utf-8");
    }

    private Portfolio getPortfolio() {
        return machineCollectors.get(0).getPortfolio();
    }
}
