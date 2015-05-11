package resultWriters;

import model.MachineStatesCollector;
import model.PortfolioStateCollector;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public class PortfolioDataWriter extends ResultWriter {

    public PortfolioDataWriter(PortfolioStateCollector portfolioCollector, String fileName) {
        super(null, portfolioCollector, fileName);
    }

    @Override
    public void writeTo(PrintWriter writer) {

        writer.write(portfolioCollector.printHead());
        portfolioCollector.writeStatesTo(writer);
    }
}
