package trade.researcher.tryOut.model;

import trade.tryOut.resultWriters.*;
import trade.core.tools.*;
import trade.core.model.Portfolio;

import java.io.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */
public class ResultWriterStub extends ResultWriter {

    public ResultWriterStub() {
        super(null);
    }

    @Override
    protected void writeTo(PrintWriter writer) {
    }

    @Override
    protected Portfolio getPortfolio() {
        return null;
    }

    @Override
    protected int getYear() {
        return 0;
    }

    public void write() throws Throwable {
        Log.info("ResultWriterStub writes nothing");
    }
}
