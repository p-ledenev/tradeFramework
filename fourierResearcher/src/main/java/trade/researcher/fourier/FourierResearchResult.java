package trade.researcher.fourier;

import lombok.*;
import trade.core.tools.Round;
import trade.researcher.tryOut.model.IResearchResult;

@Getter @Setter
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