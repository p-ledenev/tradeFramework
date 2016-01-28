package model;

import lombok.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */

@Data
@AllArgsConstructor
public class ResearchResult {

	private double sieveParam;
	private int gapsNumber;

	private double profit;
	private double loss;
	private double tradeCoefficient;

	public boolean greater(ResearchResult maxResult) {
		return this.profit > maxResult.profit;
	}
}
