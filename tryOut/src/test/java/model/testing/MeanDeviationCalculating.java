package model.testing;

import dataSources.*;
import model.*;
import org.junit.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 30.06.2015.
 */

@Ignore
public class MeanDeviationCalculating {

    @Test
    public void computeMeanDeviation() throws Throwable {

        IDataSource source = DataSourceFactory.createDataSource();

        Integer[] years = {2010, 2011, 2012, 2013, 2014, 2015};

        for (Integer year : years) {
            String path = "d:\\Projects\\Alfa\\java\\analysis\\sources\\" + year + "\\";
            List<TryOutCandle> candles = source.readCandlesFrom(path + "usd_1min.txt");

            CandlesIterator iterator = new CandlesIterator(candles);
            double deviation = iterator.countMeanDeviation(year);

            Log.info("Deviation for year " + year + " (%): " + Round.toAmount(deviation * 100, 4));
        }
    }
}
