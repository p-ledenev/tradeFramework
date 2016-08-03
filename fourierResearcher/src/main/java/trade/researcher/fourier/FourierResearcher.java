package trade.researcher.fourier;

import trade.researcher.tryOut.model.*;
import trade.tryOut.dataSources.*;
import trade.core.model.*;
import trade.tryOut.run.Trader;
import trade.tryOut.model.*;
import trade.researcher.tryOut.writers.DataWriterStrategy;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 15.12.2015.
 */
public class FourierResearcher {

	//public static String settingPath = "d:/Projects/Alfa/java/tradeFramework/tryOutResearcher/data/";
	public static String settingPath = "./";
	public static String paramsFile = "params.fourier.researcher.txt";

	public static void main(String[] args) throws Throwable {

		List<FourierInitialSettings> settingsList = readSettings(settingPath, "settings.fourier.researcher.txt");

		List<Double> frequencies = FrequencyParamsReader.read(settingPath + "/" + paramsFile).buildParams();
		List<Double> depths = DepthParamsReader.read(settingPath + "/" + paramsFile).buildParams();

		for (FourierInitialSettings settings : settingsList) {

			DataWriterStrategy writerStrategy = new FourierTecplotDataWriterStrategy(
					settings.getStrategyName(),
					depths.size(),
					frequencies.size());

			ResearcherDataWriter researcherDataWriter = new ResearcherDataWriter(
					writerStrategy,
					settings.getStrategyName(),
					settings.getSecurity(),
					settings.isIntradayTrading());

			for (Double frequency : frequencies) {
				for (Double depth : depths) {

					String year = settings.getYear();

					List<TryOutCandle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
							settingPath + IDataSource.sourceFolder + "/" + year +
									"/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

					IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(Integer.parseInt(year));

					CandlesIterator candlesIterator = new CandlesIterator(candles);

					Portfolio portfolio = settings.initPortfolio(depth.intValue(), frequency.intValue());
					TradeDataCollector tradeDataCollector = new NoWritingTradeDataCollector(portfolio);

					Trader trader = new Trader(candlesIterator, tradeDataCollector, ordersExecutor, portfolio);
					trader.trade();

					double loss = tradeDataCollector.computeMaxLossesPercent();
					double profit = tradeDataCollector.computeEndPeriodMoneyPercent();

					FourierResearchResult result = new FourierResearchResult(
							frequency.intValue(),
							depth.intValue(),
							profit,
							loss,
							profit / loss);

					researcherDataWriter.addResearchResult(result);
					researcherDataWriter.appendToFile();
				}
			}
		}
	}

	private static List<FourierInitialSettings> readSettings(String path, String fileName) throws Throwable {

		List<FourierInitialSettings> settings = new ArrayList<>();

		BufferedReader reader = new BufferedReader(new FileReader(new File(path + "/" + fileName)));

		String line;
		while ((line = reader.readLine()) != null)
			settings.add(FourierInitialSettings.createFrom(line));

		return settings;
	}
}
