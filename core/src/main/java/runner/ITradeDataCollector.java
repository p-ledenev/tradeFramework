package runner;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public interface ITradeDataCollector {

    void collect();

    ITradeDataWriter getResultWriter();
}
