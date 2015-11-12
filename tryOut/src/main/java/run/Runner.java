package run;

import dataSources.*;
import model.*;
import settings.*;
import tools.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<InitialSettings> settingsList = readSettings();
        for (InitialSettings settings : settingsList) {
            for (String year : settings.getYears()) {

                List<TryOutCandle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
                        IDataSource.sourcePath + "/" + year + "/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

                IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(Integer.parseInt(year));

                CandlesIterator candlesIterator = new CandlesIterator(candles);

                Portfolio portfolio = settings.initPortfolio(candles);
                TradeDataCollector dataCollector = createDataCollector(portfolio);

                Trader trader = new Trader(candlesIterator, dataCollector, ordersExecutor, portfolio);
                executor.execute(trader);
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        Log.info("All threads are done");
    }

    private static List<InitialSettings> readSettings() throws Throwable {
        List<InitialSettings> settings = new ArrayList<InitialSettings>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(InitialSettings.settingPath + "settings.txt")));

        String line;
        while ((line = reader.readLine()) != null)
            settings.add(InitialSettings.createFrom(line));

        return settings;
    }

    private static TradeDataCollector createDataCollector(Portfolio portfolio) {

        TradeDataCollector dataCollector;
        if (portfolio.countMachines() == 1)
            dataCollector = new SingleMachineTradeDataCollector(portfolio);
        else
            dataCollector = new CommonTradeDataCollector(portfolio);

        return dataCollector;
    }
}
