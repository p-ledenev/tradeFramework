package model;

import resultWriters.*;

import java.util.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */
public class NoWritingTradeDataCollector extends TradeDataCollector {

    public NoWritingTradeDataCollector(Portfolio portfolio) {
        super(portfolio);
    }

    @Override
    protected List<ResultWriter> collectResultWriters() {
        return null;
    }

    @Override
    protected void collectMachinesTradeData(List<Order> orders) {

    }

    public TradeDataWriter getResultWriter() {
        List<ResultWriter> resultWriters = new ArrayList<ResultWriter>();

        resultWriters.add(new ResultWriterStub());

        return new TradeDataWriter(resultWriters);
    }
}
