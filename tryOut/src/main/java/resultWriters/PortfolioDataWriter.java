package resultWriters;

import model.*;

import java.io.*;

/**
 * Created by DiKey on 11.05.2015.
 */
public class PortfolioDataWriter extends ResultWriter {

    private PortfolioMoneyStatesCollector statesCollector;

    public PortfolioDataWriter(PortfolioMoneyStatesCollector statesCollector, String fileName) {
        super(fileName);

        this.statesCollector = statesCollector;
    }

    @Override
    public void writeTo(PrintWriter writer) {

        writer.write(statesCollector.printHead() + "\n");
        statesCollector.writeStatesTo(writer);
    }

    @Override
    protected Portfolio getPortfolio() {
        return statesCollector.getEntity();
    }

    @Override
    protected int getYear() {
        return statesCollector.getYear();
    }
}
