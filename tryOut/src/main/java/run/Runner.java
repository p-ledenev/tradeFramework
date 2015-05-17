package run;

import dataSources.IDataSource;
import dataSources.DataSourceFactory;
import model.*;
import runner.ICandlesIterator;
import runner.ITradeDataCollector;
import runner.Trader;
import settings.InitialSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<InitialSettings> settingsList = readSettings();
        for (InitialSettings settings : settingsList) {
            for (String year : settings.getYears()) {

                Portfolio portfolio = settings.initPortfolio();

                List<Candle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
                        IDataSource.sourcePath + "/" + year + "/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

                IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(candles);

                ICandlesIterator candlesIterator = new CandlesIterator(candles);
                ICandlesProcessor candlesProcessor = new CandlesProcessor(portfolio, ordersExecutor);

                ITradeDataCollector dataCollector = createDataCollector(portfolio);

                Trader trader = new Trader(candlesIterator, candlesProcessor, dataCollector);
                trader.trade();
            }
        }
    }

    private static List<InitialSettings> readSettings() throws Throwable {
        List<InitialSettings> settings = new ArrayList<InitialSettings>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(InitialSettings.settingPath + "/settings.txt")));

        String line;
        while ((line = reader.readLine()) != null)
            settings.add(InitialSettings.createFrom(line));

        return settings;
    }

    private static ITradeDataCollector createDataCollector(Portfolio portfolio) {

        ITradeDataCollector dataCollector;
        if (portfolio.countMachines() == 1)
            dataCollector = new SingleMachineTradeDataCollector(portfolio);
        else
            dataCollector = new CommonTradeDataCollector(portfolio);

        return dataCollector;
    }
}
