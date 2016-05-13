package decisionStrategies.algorithmic;

import decisionStrategies.DecisionStrategy;
import fourierConstructors.*;
import model.*;
import org.apache.commons.math3.complex.Complex;

import java.util.*;

/**
 * Created by ledenev.p on 10.05.2016.
 */

public class FourierPreprocessingDecisionStrategy extends DecisionStrategy {

	private DecisionStrategy decisionStrategy;
	private IFourierConstructor constructor;

	public FourierPreprocessingDecisionStrategy(int frequenciesNumber, DecisionStrategy decisionStrategy) {
		this.decisionStrategy = decisionStrategy;
		constructor = FourierConstructorFactory.createLastValueTailConstructor(frequenciesNumber);
	}

	@Override
	public Direction computeOrderDirection(Candle[] candles) {
		Candle[] newCandles = addFourierAverage(candles);
		return decisionStrategy.computeOrderDirection(newCandles);
	}

	protected Candle[] addFourierAverage(Candle[] candles) {
		int depth = candles.length;

		int start = (decisionStrategy.getCandlesStorage().size() + 2 > depth - 1)
				? decisionStrategy.getCandlesStorage().size() + 2
				: depth - 1;

		List<Candle> response = new ArrayList<>();
		for (int i = start; i < candlesStorage.size(); i++) {

			double averageValue = computeAverageFor(createCandleArrayBy(i, depth));
			Candle candle = new Candle(candlesStorage.get(i).getDate(), averageValue);

			decisionStrategy.getCandlesStorage().add(candle);
			response.add(candle);
		}

		return response.toArray(new Candle[response.size()]);
	}

	@Override
	public String[] getStateParamsHeader() {
		List<String> response = new ArrayList<>();
		response.add("average");
		response.addAll(Arrays.asList(decisionStrategy.getStateParamsHeader()));

		return response.toArray(new String[response.size()]);
	}

	@Override
	protected String[] collectCurrentStateParams() {
		List<String> response = new ArrayList<>();
		response.add(getLastFourierAverage().toString());
		response.addAll(Arrays.asList(decisionStrategy.getStateParamsHeader()));

		return response.toArray(new String[response.size()]);
	}

	@Override
	public int getInitialStorageSizeFor(int depth) {
		return depth + 1 + decisionStrategy.getInitialStorageSizeFor(depth);
	}

	@Override
	public boolean hasCurrentState() {
		return decisionStrategy.getCandlesStorage().size() != 0;
	}

	private Double getLastFourierAverage() {
		return decisionStrategy.getLastCandle().getValue();
	}

	protected double computeAverageFor(Candle[] values) {
		double[] doubleValues = new double[values.length];
		for (int i = 0; i < values.length; i++)
			doubleValues[i] = values[i].getValue();

		Complex[] result = constructor.transform(doubleValues);
		return result[values.length - 1].getReal();
	}
}
