package settings;

import decisionStrategies.DecisionStrategy;
import decisionStrategies.algorithmic.AveragingDecisionStrategy;
import decisionStrategies.neuron.NeuronDecisionStrategy;
import decisionStrategies.neuronTraining.NeuronTrainingDecisionStrategy;
import lombok.Data;
import model.*;
import siftStrategies.*;

import java.util.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */

@Data
public class InitialSettings {

	//public static String settingPath = "F:/Teddy/Alfa/java/v1.0/tradeFramework/tryOut/data/";
	//public static String settingPath = "d:/Projects/Alfa/java/tradeFramework/tryOut/data/";
	public static String settingPath = "./";

	private String security;
	private String timeFrame;
	private double sieveParam;
	private List<Integer> fillingGapsNumbers;
	private List<Integer> depths;
	private List<String> years;
	private String strategyName;
	private double commission;
	private boolean intradayTrading;

	public InitialSettings() {
		depths = new ArrayList<Integer>();
		years = new ArrayList<String>();
		fillingGapsNumbers = new ArrayList<Integer>();
		intradayTrading = false;
	}

	public void addDepth(int depth) {
		depths.add(depth);
	}

	public void addFillingGapsNumber(int fillingGapNumber) {
		fillingGapsNumbers.add(fillingGapNumber);
	}

	public static InitialSettings createFrom(String line) {

		String[] data = line.split("~&~");

		InitialSettings settings = new InitialSettings();

		settings.setStrategyName(data[2]);
		settings.setSecurity(data[0]);
		settings.setTimeFrame(data[1]);
		settings.setSieveParam(Double.parseDouble(data[4]));
		settings.setCommission(Double.parseDouble(data[3]));
		settings.setYears(Arrays.asList(data[6].split(";")));

		String[] strGaps = data[5].split(";");
		for (String depth : strGaps)
			settings.addFillingGapsNumber(Integer.parseInt(depth));

		String[] strDepths = data[7].split(";");
		for (String depth : strDepths)
			settings.addDepth(Integer.parseInt(depth));

		if (data.length == 9 && data[8].equals("intraday"))
			settings.setIntradayTrading(true);

		return settings;
	}

	public Portfolio initPortfolio(double sieveParam, int fillingGapsNumber) throws Throwable {
		ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam, fillingGapsNumber);
		CandlesStorage candlesStorage = new TryOutCandlesStorage(siftStrategy);

		Portfolio portfolio = new Portfolio(strategyName, security, intradayTrading, candlesStorage);

		for (int depth : depths) {
			DecisionStrategy decisionStrategy = DecisionStrategy.createFor(strategyName, candlesStorage);
			Machine machine = Machine.with(portfolio, decisionStrategy, commission, depth);
			portfolio.addMachine(machine);
		}

		return portfolio;
	}

	public Portfolio initPortfolio(List<TryOutCandle> allData, int fillingGapsNumber) throws Throwable {

		Portfolio portfolio = initPortfolio(sieveParam, fillingGapsNumber);

		for (Machine machine : portfolio.getMachines())
			setSpecificParamsFor(allData, machine);

		return portfolio;
	}

	private void setSpecificParamsFor(List<TryOutCandle> allData, Machine machine) throws Throwable {

		DecisionStrategy strategy = machine.getDecisionStrategy();
		ISiftCandlesStrategy siftStrategy = strategy.getSiftStrategy();

		if (strategy instanceof NeuronTrainingDecisionStrategy) {
			Candle[] candlesArray = allData.toArray(new Candle[allData.size()]);
			CandlesStorage allDataStorage = new CandlesStorage(siftStrategy, Arrays.asList(candlesArray));
			((NeuronTrainingDecisionStrategy) strategy).setAllDataStorage(allDataStorage);
		}

		if (strategy instanceof NeuronDecisionStrategy) {
			NeuronDecisionStrategy neuronStrategy = (NeuronDecisionStrategy) strategy;
			neuronStrategy.readNetwork(settingPath + neuronStrategy.getNetworkName() + ".eg");

			AveragingDecisionStrategy averagingStrategy = new AveragingDecisionStrategy();
			averagingStrategy.setCandlesStorage(strategy.getCandlesStorage());

			neuronStrategy.setAveragingStrategy(averagingStrategy);
		}

		if (strategy instanceof NeuronTrainingDecisionStrategy) {
			NeuronTrainingDecisionStrategy neuronStrategy = (NeuronTrainingDecisionStrategy) strategy;

			AveragingDecisionStrategy averagingStrategy = new AveragingDecisionStrategy();
			averagingStrategy.setCandlesStorage(strategy.getCandlesStorage());

			neuronStrategy.setAveragingStrategy(averagingStrategy);
		}
	}
}