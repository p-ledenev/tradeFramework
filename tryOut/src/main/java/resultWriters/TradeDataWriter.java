package resultWriters;

import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Created by DiKey on 16.05.2015.
 */

@AllArgsConstructor
public class TradeDataWriter {

    private List<ResultWriter> writers;

    public void writeData() throws Throwable {
        for (ResultWriter writer : writers)
            writer.write();
    }
}
