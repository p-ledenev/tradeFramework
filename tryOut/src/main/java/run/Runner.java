package run;

import dataSources.*;
import model.*;
import settings.*;
import tools.*;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Runner {

	public static void main(String[] args) throws Throwable {

		//ExecutorService executor = Executors.newFixedThreadPool(1);

		List<InitialSettings> settingsList = readSettings(InitialSettings.settingPath);

		for (InitialSettings settings : settingsList) {
			for (String year : settings.getYears()) {
				for (Integer fillingGapsNumber : settings.getFillingGapsNumbers()) {

					List<TryOutCandle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
							IDataSource.sourcePath + "/" + year + "/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

					IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(Integer.parseInt(year));

					CandlesIterator candlesIterator = new CandlesIterator(candles);

					Portfolio portfolio = settings.initPortfolio(candles, fillingGapsNumber);
					TradeDataCollector dataCollector = createDataCollector(portfolio);

					Trader trader = new Trader(candlesIterator, dataCollector, ordersExecutor, portfolio);
					// executor.execute(trader);
					trader.trade();
				}
			}
		}

		// executor.shutdown();
		// while (!executor.isTerminated()) {
		// }

		Log.info("All threads are done");
	}

	public static List<InitialSettings> readSettings(String path, String fileName) throws Throwable {

		List<InitialSettings> settings = new ArrayList<InitialSettings>();

		BufferedReader reader = new BufferedReader(new FileReader(new File(path + "/" + fileName)));

		String line;
		while ((line = reader.readLine()) != null)
			settings.add(InitialSettings.createFrom(line));

		return settings;
	}

	public static List<InitialSettings> readSettings(String path) throws Throwable {
		return readSettings(path, "settings.txt");
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
