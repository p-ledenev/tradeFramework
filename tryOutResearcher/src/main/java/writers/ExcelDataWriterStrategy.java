package writers;

import model.ResearchResult;
import tools.Round;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public class ExcelDataWriterStrategy implements DataWriterStrategy {

	@Override
	public String printHeader() {
		return "gapsNumber;sieveParam;profit;loss;tradeCoefficient";
	}

	@Override
	public String print(ResearchResult result) throws Throwable {
		return result.getGapsNumber() + ";" + Round.toSignificant(result.getSieveParam())
				+ ";" + Round.toMoneyAmount(result.getProfit()) + ";" + Round.toMoneyAmount(result.getLoss())
				+ ";" + Round.toMoneyAmount(result.getTradeCoefficient());
	}

	@Override
	public String getFileExtension() {
		return "csv";
	}


}
