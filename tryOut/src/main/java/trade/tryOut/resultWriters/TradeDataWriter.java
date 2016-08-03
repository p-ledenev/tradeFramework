package trade.tryOut.resultWriters;

import lombok.*;

import java.util.*;

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
