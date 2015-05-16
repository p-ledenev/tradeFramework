package resultWriters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import runner.ITradeDataWriter;

import java.util.List;

/**
 * Created by DiKey on 16.05.2015.
 */

@AllArgsConstructor
public class TradeDataWriter implements ITradeDataWriter {

    private List<ResultWriter> writers;

    public void writeAllData() throws Throwable {
        for (ResultWriter writer : writers)
            writer.write();
    }

    public void writeNewData() throws Throwable {
    }
}
