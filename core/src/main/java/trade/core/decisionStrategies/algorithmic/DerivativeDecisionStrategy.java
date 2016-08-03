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
public abstract class DerivativeDecisionStrategy extends DecisionStrategy {

	protected List<Double> averageValues;
	protected List<Double> derivatives;
	protected List<Double> averageDerivatives;

	public DerivativeDecisionStrategy() {
		averageValues = new ArrayList<>();
		derivatives = new ArrayList<>();
		averageDerivatives = new ArrayList<>();
	}

	@Override
	public Direction computeOrderDirection(Candle[] candles) {

		addDerivatives(candles);

		double averageDerivative = getLastAverageDerivative();

		if (averageDerivative > 0)
			return Direction.Buy;

		if (averageDerivative < 0)
			return Direction.Sell;

		return Direction.Neutral;
	}

	@Override
	public boolean couldOpenPosition(int depth, Position previousPosition) {
		return Math.abs(getLastAverageDerivative()) * 100000 > depth * 0.05;
	}

	@Override
	public String[] getStateParamsHeader() {
		return new String[]{
				// "average", "derivative", "averageDerivative"
				"average", "averageDerivative"
		};
	}

	@Override
	protected String[] collectCurrentStateParams() {
		return new String[]{
				Double.toString((int) getLastAverageValue()),
				//Double.toString(Round.toSignificant(getLastDerivativeValue() * 10000)),
				Double.toString(Round.toSignificant(getLastAverageDerivative() * 100000))
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

			double averageDerivative = 0;
			if (derivatives.size() >= depth) {
				averageDerivative = computeAverageFor(fromEnd(derivatives, depth));
			}

			averageDerivatives.add(averageDerivative);
		}
	}

	protected double computeAverageFor(Candle[] values) {
		double[] doubleValues = new double[values.length];
		for (int i = 0; i < values.length; i++)
			doubleValues[i] = values[i].getValue();

		return computeAverageFor(doubleValues);
	}

	protected abstract double computeAverageFor(double[] values);

	private double[] fromEnd(List<Double> list, int depth) {
		double[] response = new double[depth];
		for (int i = 0; i < depth; i++)
			response[i] = list.get(list.size() - depth + i);

		return response;
	}

	public double getLastAverageValue() {
		if (averageValues.size() == 0)
			return 0;

		return averageValues.get(averageValues.size() - 1);
	}

	public double getLastAverageDerivative() {
		if (averageDerivatives.size() == 0)
			return 0;

		return averageDerivatives.get(averageDerivatives.size() - 1);
	}

	public List<Double> getAverageValues(int depth) {
		int size = averageValues.size();

		List<Double> result = new ArrayList<>();
		if (size < depth)
			return result;

		for (int i = 0; i < depth; i++)
			result.add(averageValues.get(size - depth + i));

		return result;
	}

	@Override
	public boolean hasCurrentState() {
		return averageDerivatives.size() != 0;
	}
}
