package trade.terminals.core.settings;

import lombok.Getter;
import trade.core.decisionStrategies.DecisionStrategy;
import trade.core.model.*;
import trade.core.siftStrategies.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class ConciseFormatPortfolioBuilder implements IPortfolioBuilder {

	private static double standardCommission = 0.5;
	private static String intraday = "intraday";

	private String security;
	private String decisionStrategyName;
	private double sieveParam;
	private int fillingGapsNumber;
	private boolean intradayTrading;

	@Getter
	private Portfolio portfolio;

	private void init(String line) {
		String[] params = line.split(";");

		security = params[0];
		decisionStrategyName = params[1];
		sieveParam = Double.parseDouble(params[2]);
		fillingGapsNumber = Integer.parseInt(params[3]);
		intradayTrading = params[4].equals(intraday);
	}

	public void build(String line) {
		init(line);

		ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam, fillingGapsNumber);
		portfolio = new Portfolio(
				decisionStrategyName + "_" + security,
				security,
				1,
				intradayTrading,
				new CandlesStorage(siftStrategy));
	}

	public void addMachine(String line) throws Throwable {
		DecisionStrategy decisionStrategy = DecisionStrategy.createFor(decisionStrategyName, portfolio.getCandlesStorage());

		ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(line);
		builder.build(portfolio, decisionStrategy, standardCommission);

		portfolio.addMachine(builder.getMachine());
	}

	public String serialize() {
		String result = security + ";" + decisionStrategyName + ";" + sieveParam + ";"
				+ fillingGapsNumber + ";" + (intradayTrading ? intraday : "continuous") + "\n";

		for (Machine machine : portfolio.getMachines()) {
			ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(machine);
			result += builder.serialize() + "\n";
		}

		return result;
	}

	public boolean isMachineBlocked(String line) throws Throwable {
		ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(line);

		return builder.isBlocked();
	}
}
