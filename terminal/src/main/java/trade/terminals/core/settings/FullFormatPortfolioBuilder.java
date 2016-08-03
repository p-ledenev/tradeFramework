package trade.terminals.core.settings;

import lombok.Getter;
import trade.core.decisionStrategies.DecisionStrategy;
import trade.core.model.*;
import trade.core.siftStrategies.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class FullFormatPortfolioBuilder implements IPortfolioBuilder {

	private String security;
	private String title;
	private String decisionStrategyName;
	private int lot;
	private double sieveParam;
	private int fillingGapsNumber;
	private double commission;
	private boolean intradayTrading;

	@Getter
	private Portfolio portfolio;

	private void init(String line) {

		String[] params = line.split("\\t");

		security = params[0];
		title = params[1];
		decisionStrategyName = params[2];
		lot = Integer.parseInt(params[3]);
		sieveParam = Double.parseDouble(params[4]);
		fillingGapsNumber = Integer.parseInt(params[5]);
		commission = Double.parseDouble(params[6]);
		intradayTrading = false;
	}

	public void build(String line) {
		init(line);

		ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam, fillingGapsNumber);
		portfolio = new Portfolio(
				title,
				security,
				lot,
				intradayTrading,
				new CandlesStorage(siftStrategy));
	}

	public void addMachine(String line) throws Throwable {

		DecisionStrategy decisionStrategy = DecisionStrategy.createFor(decisionStrategyName, portfolio.getCandlesStorage());

		FullFormatMachineBuilder builder = new FullFormatMachineBuilder(line);
		builder.build(portfolio, decisionStrategy, commission);

		portfolio.addMachine(builder.getMachine());
	}

	public String serialize() {
		String result = security + "\t" + title + "\t" + decisionStrategyName + "\t" + lot +
				"\t" + sieveParam + "\t" + fillingGapsNumber + "\t" + commission + "\n";

		for (Machine machine : portfolio.getMachines()) {
			FullFormatMachineBuilder builder = new FullFormatMachineBuilder(machine);
			result += builder.serialize() + "\n";
		}

		return result;
	}

	public boolean isMachineBlocked(String line) throws Throwable {
		FullFormatMachineBuilder builder = new FullFormatMachineBuilder(line);

		return builder.isBlocked();
	}
}
