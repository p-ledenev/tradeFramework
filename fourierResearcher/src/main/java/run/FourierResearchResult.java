package run;

import lombok.*;
import model.IResearchResult;
import tools.Round;

@Data
@AllArgsConstructor
public class FourierResearchResult implements IResearchResult {

	private int frequency;
	private int depth;

	private double profit;
	private double loss;
	private double tradeCoefficient;

	@Override
	public String print(String separator) {
		return depth + separator +
				frequency + separator +
				Round.toMoneyAmount(profit) + separator +
				Round.toMoneyAmount(loss) + separator +
				Round.toMoneyAmount(tradeCoefficient);
	}
}