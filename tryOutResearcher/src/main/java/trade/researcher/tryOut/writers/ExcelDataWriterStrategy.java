package trade.researcher.tryOut.writers;

import trade.researcher.tryOut.model.IResearchResult;

/**
 * Created by ledenev.p on 28.01.2016.
 */
public class ExcelDataWriterStrategy implements DataWriterStrategy {

	@Override
	public String printHeader() {
		return "gapsNumber;sieveParam;profit;loss;tradeCoefficient";
	}

	@Override
	public String print(IResearchResult result) throws Throwable {
		return result.print(";");
	}

	@Override
	public String getFileExtension() {
		return "csv";
	}


}
