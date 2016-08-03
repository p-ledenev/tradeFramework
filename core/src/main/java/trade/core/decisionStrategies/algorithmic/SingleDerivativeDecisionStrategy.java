package trade.core.decisionStrategies.algorithmic;

import lombok.Getter;
import trade.core.decisionStrategies.DecisionStrategy;
import trade.core.model.*;
import trade.core.tools.Round;

import java.util.*;

/**
 * Created by ledenev.p on 28.04.2016.
 */

@Getter
public abstract class SingleDerivativeDecisionStrategy extends DecisionStrategy {

	protected List<Double> averageValues;
	protected List<Double> derivatives;

	public SingleDerivativeDecisionStrategy() {
		averageValues = new ArrayList<>();
		derivatives = new ArrayList<>();
	}

	@Override
	public Direction computeOrderDirection(Candle[] candles) {

		addDerivatives(candles);

		double derivative = getLastDerivative();

		if (derivative > 0)
			return Direction.Buy;

		if (derivative < 0)
			return Direction.Sell;

		return Direction.Neutral;
	}

	@Override
	public boolean couldOpenPosition(int depth, Position previousPosition) {
		return Math.abs(getLastDerivative()) * 100000 > depth * 0.05;
	}

	@Override
	public String[] getStateParamsHeader() {
		return new String[]{
				"average", "derivative"
		};
	}

	@Override
	protected String[] collectCurrentStateParams() {
		return new String[]{
				Double.toString((int) getLastAverageValue()),
				Double.toString(Round.toSignificant(getLastDerivative() * 100000))
		};
	}

	@Override
	public int getInitialStorageSizeFor(int depth) {
		return depth * 2 + 3;
	}

	protected void addDerivatives(Candle[] candles) {
		int depth = candles.length;

		int start = (derivatives.size() + 2 > depth - 1) ? derivatives.size() + 2 : depth - 1;
		for (int i = start; i < candlesStorage.size(); i++) {

			double averageValue = computeAverageFor(createCandleArrayBy(i, depth));
			double derivativeValue = (averageValue - getLastAverageValue()) / averageValue;

			averageValues.add(averageValue);
			derivatives.add(derivativeValue);
		}
	}

	protected double computeAverageFor(Candle[] values) {
		double[] doubleValues = new double[values.length];
		for (int i = 0; i < values.length; i++)
			doubleValues[i] = values[i].getValue();

		return computeAverageFor(doubleValues);
	}

	protected abstract double computeAverageFor(double[] values);


	public double getLastAverageValue() {
		if (averageValues.size() == 0)
			return 0;

		return averageValues.get(averageValues.size() - 1);
	}

	public double getLastDerivative() {
		if (derivatives.size() == 0)
			return 0;

		return derivatives.get(derivatives.size() - 1);
	}

	@Override
	public boolean hasCurrentState() {
		return derivatives.size() != 0;
	}
}
