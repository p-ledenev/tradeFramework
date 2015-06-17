package resultWriters;

import lombok.Data;
import model.MachinePositionsCollector;
import model.Portfolio;
import model.Position;
import model.StrategyStatesCollector;

import java.io.PrintWriter;

/**
 * Created by DiKey on 11.05.2015.
 */

@Data
public class DetailMachineDataWriter extends ResultWriter {

    private MachinePositionsCollector positionsCollector;
    private StrategyStatesCollector statesCollector;

    public DetailMachineDataWriter(MachinePositionsCollector positionsCollector, StrategyStatesCollector statesCollector, String fileName) {
        super(fileName);

        this.positionsCollector = positionsCollector;
        this.statesCollector = statesCollector;
    }

    @Override
    protected void writeTo(PrintWriter writer) {

        writer.write(printHeaders() + "\n");

        for (int i = 0; i < statesCollector.collectionSize(); i++)
            writer.write(statesCollector.get(i).printCSV() + "; ;" + printPosition(i) + "\n");
    }

    private String printPosition(int i) {

        if (!positionsCollector.hasPosition(i))
            return "";

        Position position = positionsCollector.get(i);

        String response = position.printCSV() + ";";

        if (position.isBuy())
            response += position.getValue() + ";;";

        if (position.isSell())
            response += ";" + position.getValue() + ";";

        if (position.isNeutral())
            response += ";;" + position.getValue();

        return response;
    }

    @Override
    protected Portfolio getPortfolio() {
        return positionsCollector.getPortfolio();
    }


    @Override
    protected int getYear() {
        return positionsCollector.getYear();
    }

    private String printHeaders() {
        return statesCollector.printHeader() + ";dateIndex;date;openBuy;openSell;close;";
    }
}
