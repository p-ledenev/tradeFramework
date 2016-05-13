package run;

import decisionStrategies.DecisionStrategy;
import decisionStrategies.algorithmic.*;
import model.*;
import settings.InitialSettings;
import siftStrategies.*;

/**
 * Created by ledenev.p on 06.05.2016.
 */
public class FourierInitialSettings {

	private InitialSettings settings;

	public static FourierInitialSettings createFrom(String line) {
		FourierInitialSettings settings = new FourierInitialSettings();
		settings.settings = InitialSettings.createFrom(line);

		return settings;
	}

	public String getStrategyName() {
		return settings.getStrategyName();
	}

	public String getSecurity() {
		return settings.getSecurity();
	}

	public Boolean isIntradayTrading() {
		return settings.isIntradayTrading();
	}

	public String getTimeFrame() {
		return settings.getTimeFrame();
	}

	public String getYear() {
		return settings.getYears().get(0);
	}

	private Double getCommission() {
		return settings.getCommission();
	}

	public Portfolio initPortfolio(int depth, int frequency) throws Throwable {

		int fillingGapsNumber = settings.getFillingGapsNumbers().get(0);
		double sieveParam = settings.getSieveParam();

		ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam, fillingGapsNumber);
		CandlesStorage candlesStorage = new TryOutCandlesStorage(siftStrategy);

		Portfolio portfolio = new Portfolio(getStrategyName(), getSecurity(), isIntradayTrading(), candlesStorage);

		DecisionStrategy decisionStrategy = new SingleFourierDecisionStrategy(frequency);
		decisionStrategy.setCandlesStorage(candlesStorage);

		Machine machine = Machine.with(portfolio, decisionStrategy, getCommission(), depth);
		portfolio.addMachine(machine);

		return portfolio;
	}
}
