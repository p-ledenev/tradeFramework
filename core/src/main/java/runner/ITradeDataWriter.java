package runner;

/**
 * Created by ledenev.p on 15.05.2015.
 */
public interface ITradeDataWriter {

    void writeAllData() throws Throwable;

    void writeNewData() throws Throwable;
}
