package model;

import lombok.*;
import tools.Round;

/**
 * Created by ledenev.p on 16.12.2015.
 */

@Data
@AllArgsConstructor
public class ResearchResult implements IResearchResult {

	private double sieveParam;
	private int gapsNumber;

	private double profit;
	private double loss;
	private double tradeCoefficient;

	public boolean greater(ResearchResult maxResult) {
		return this.profit > maxResult.profit;
	}

	@Override
	public String print(String separator) {
		return gapsNumber + separator +
				Round.toSignificant(sieveParam) + separator +
				Round.toMoneyAmount(profit) + separator +
				Round.toMoneyAmount(loss) + separator +
				Round.toMoneyAmount(tradeCoefficient);
	}
}
