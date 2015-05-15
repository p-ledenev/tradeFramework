package runner;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public interface ICollectTradeData {

    void collect();

    IResultWriter getResultWriter();
}
