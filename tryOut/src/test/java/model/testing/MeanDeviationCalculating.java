package model.testing;

import dataSources.*;
import model.*;
import org.junit.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 30.06.2015.
 */

//@Ignore
public class MeanDeviationCalculating {

    Integer[] years;

    @Before
    public void setUp() throws Throwable {
        years = new Integer[]{2015};
    }

    @Test
    public void computeMeanDeviation() throws Throwable {

        for (Integer year : years) {

            List<TryOutCandle> candles = readDataFor(year);

            CandlesIterator iterator = new CandlesIterator(candles);
            double deviation = iterator.computeMeanDeviation(year);

            Log.info("Deviation for year " + year + " (%): " + Round.toAmount(deviation * 100, 4));
        }
    }

    @Test
    public void computeSigma() throws Throwable {

        for (Integer year : years) {

            List<TryOutCandle> candles = readDataFor(year);

            CandlesIterator iterator = new CandlesIterator(candles);
            double sigma = iterator.computeSigma(year);

            Log.info("Sigma for year " + year + " (%): " + Round.toAmount(sigma, 4));
        }
    }

    private List<TryOutCandle> readDataFor(Integer year) throws Throwable {

        IDataSource source = DataSourceFactory.createDataSource();

        String path = "d:\\Projects\\Alfa\\java\\analysis\\sources\\" + year + "\\";

        return source.readCandlesFrom(path + "usd_1min.txt");
    }
}
