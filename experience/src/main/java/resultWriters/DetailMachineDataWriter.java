package resultWriters;

import model.*;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class DetailMachineDataWriter implements IResultWriter {

    private
    private String fileName;

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

        return new PrintWriter(TradeResultsWriter.resultPath + "\\" + fileName + "_" + security + "_" + year + "_" + title + ".csv", "utf-8");
    }

    private Portfolio getPortfolio() {
        return machineCollectors.get(0).getPortfolio();
    }

    private String printHeaders() {
        return "dateIndex|date|candleValue| |dateIndex|date|openBuy|openSell|closeBuy|closeSell|";
    }

    private List<Candle> getCandles() {
        return machineCollectors.get(0).getEntity().getCandles();
    }

    private List<State> getMachineStates() {
        return
    }
}
