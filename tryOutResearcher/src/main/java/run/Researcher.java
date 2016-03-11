package run;

import dataSources.*;
import model.*;
import settings.*;

import java.util.*;

/**
 * Created by ledenev.p on 15.12.2015.
 */
public class Researcher {

	//public static String settingPath = "d:/Projects/Alfa/java/tradeFramework/tryOutResearcher/data/";
	public static String settingPath = "./";

	public static void main(String[] args) throws Throwable {

		List<InitialSettings> settingsList = Runner.readSettings(settingPath, "settings.researcher.txt");

		for (InitialSettings settings : settingsList) {

			ResearcherDataWriter researcherDataWriter = new ResearcherDataWriter(settings.getStrategyName(), getFillGapsNumbers().size(), getSieveParams().size());

			for (Double sieveParam : getSieveParams()) {
				for (Integer gapsNumber : getFillGapsNumbers()) {

					List<ResearchResult> yearResults = new ArrayList<>();

					for (String year : settings.getYears()) {

						List<TryOutCandle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
								settingPath + IDataSource.sourceFolder + "/" + year + "/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

						IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(Integer.parseInt(year));

						CandlesIterator candlesIterator = new CandlesIterator(candles);

						Portfolio portfolio = settings.initPortfolio(sieveParam, gapsNumber);
						TradeDataCollector tradeDataCollector = new NoWritingTradeDataCollector(portfolio);

						Trader trader = new Trader(candlesIterator, tradeDataCollector, ordersExecutor, portfolio);
						trader.trade();

						double loss = tradeDataCollector.computeMaxLossesPercent();
						double profit = tradeDataCollector.computeEndPeriodMoneyPercent();

						yearResults.add(new ResearchResult(sieveParam, gapsNumber, profit, loss, profit / loss));
					}

					yearResults = removeMaxProfitYear(yearResults);
					researcherDataWriter.addResearchResult(computeAverage(yearResults, sieveParam, gapsNumber));
					researcherDataWriter.appendToFile();
				}
			}
		}
	}

	private static List<ResearchResult> removeMaxProfitYear(List<ResearchResult> results) {

		if (results.size() < 2)
			return results;

		ResearchResult maxResult = results.get(0);
		for (ResearchResult result : results)
			if (result.greater(maxResult))
				maxResult = result;

		results.remove(maxResult);

		return results;
	}

	private static ResearchResult computeAverage(List<ResearchResult> results, double sieveParam, int gapsNumber) {

		double averageCoefficient = 0;
		double averageProfit = 0;
		double averageLosses = 0;

		for (ResearchResult result : results) {

			averageCoefficient += result.getTradeCoefficient();
			averageProfit += result.getProfit();
			averageLosses += result.getLoss();
		}

		int size = results.size();
		return new ResearchResult(sieveParam, gapsNumber, averageProfit / size, averageLosses / size, averageCoefficient / size);
	}

	private static List<Double> getSieveParams() {
		List<Double> sieveParams = new ArrayList<>();

		for (double param = 0.01; param < 0.142; param += 0.002)
			sieveParams.add(param);

		return sieveParams;
	}

	private static List<Integer> getFillGapsNumbers() {

		List<Integer> gapsNumbers = new ArrayList<>();

		for (int i = 1; i < 31; i++)
			gapsNumbers.add(i);

		return gapsNumbers;
	}
}
