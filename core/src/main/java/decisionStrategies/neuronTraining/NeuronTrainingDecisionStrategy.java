package decisionStrategies.neuronTraining;

import approximationConstructors.*;
import decisionStrategies.*;
import decisionStrategies.algorithmic.*;
import lombok.*;
import model.*;
import org.joda.time.*;
import tools.*;

import java.io.*;
import java.util.*;

/**
 * Created by DiKey on 08.08.2015.
 */
public abstract class NeuronTrainingDecisionStrategy extends DecisionStrategy {

	@Setter
	protected CandlesStorage allDataStorage;

	protected Direction directionBeforeNeutral = Direction.Neutral;
	protected Direction previousDirection = Direction.Neutral;

	@Setter
	protected AveragingDecisionStrategy averagingStrategy;

	@Override
	protected Direction computeOrderDirection(Candle[] candles) {

		try {
			TrainingResult result = computeTrainingResult(candles.length);
			return result.getDirection();

		} catch (IOException e) {
			Log.error(e);
		}

		return Direction.Neutral;
	}

	public TrainingResult computeTrainingResult(int depth) throws IOException {

		//List<Candle> futureData = getVariableDataForPrediction(depth);
		List<Candle> futureData = getConstantDataForPrediction(depth);

		if (futureData.size() < depth - 1)
			return TrainingResult.empty();

		if (candlesStorage.lessThan(depth))
			return TrainingResult.empty();

		if (!candlesStorage.backTo(depth).hasSameDay(candlesStorage.last()))
			return TrainingResult.empty();

		if (!futureData.get(0).hasTimeGreaterThan(new LocalTime(10, 1)))
			return TrainingResult.empty();

		if (!candlesStorage.backTo(depth).hasTimeGreaterThan(new LocalTime(10, 1)))
			return TrainingResult.empty();

		if (!futureData.get(0).hasSameDay(futureData.get(futureData.size() - 1)))
			return TrainingResult.empty();

		Direction direction = computeDirection(futureData);

		if (!direction.equals(Direction.Neutral))
			previousDirection = direction;

		if (direction.equals(Direction.Neutral))
			directionBeforeNeutral = previousDirection;

		if (directionBeforeNeutral.equals(direction))
			direction = Direction.Neutral;

		TrainingResult result = TrainingResult.createFor(getAverageValues(depth), direction);
		//TrainingResult result = buildStatisticalResult(depth, direction);

		if (!validateHistogram(result, depth))
			return TrainingResult.empty();

		return result;
	}

	private boolean validateHistogram(TrainingResult result, int depth) throws IOException {

		Map<Double, Double> histogram = result.buildHistogram();
		FileWriter writer = new FileWriter("D://data.txt");
		for (Double key : histogram.keySet())
			writer.write(key + ";" + histogram.get(key) + "\n");

		for (Double value : averagingStrategy.getAverageValues(depth * 10))
			writer.write(Round.toMoneyAmount(value) + ";\n");

		for (Candle candle : candlesStorage.last(depth * 10))
			writer.write(candle.getValue() + ";" + Format.asString(candle.getDate()) + "\n");

		for (Double value : result.getNormalizeIncrements())
			writer.write(Round.toSignificant(value) + ";\n");

		for (Double value : result.getRowIncrements())
			writer.write(Round.toSignificant(value) + ";\n");

		writer.close();

		for (Double key : histogram.keySet()) {
			if (histogram.get(key) > 15 && candlesStorage.last().hasDateGreaterThan(DateTime.parse("2010-02-11"))) {
				int ll = 1;

				Log.info("Validation failed");

				return false;
			}
		}

		return true;
	}

	private TrainingResult buildStatisticalResult(int depth, Direction direction) {

		getAverageValues(depth);

		if (averagingStrategy.getAverageDerivatives().size() == 0)
			return TrainingResult.empty();

		List<Candle> pastData = candlesStorage.last(depth);
		double last = candlesStorage.last().getValue();

		Approximation approximation = ApproximationConstructorFactory.createConstructor().approximate(Format.toDoubleArray(pastData));

		double derivative = averagingStrategy.getLastAverageDerivative() * 100;
		double mean = (TrainingResult.mean(pastData) - last) / last;
		double app = approximation.getHighestDegreeParameter() / 100;
		double sigma = TrainingResult.sigma(pastData) / last;
		double max = (TrainingResult.max(pastData) - last) / last;
		double min = (TrainingResult.min(pastData) - last) / last;

		return TrainingResult.createWithParams(direction, derivative, app, mean, sigma, max, min);
	}

	protected abstract Direction computeDirection(List<Candle> candles);

	@Override
	public int getInitialStorageSizeFor(int depth) {
		return depth + 1;
	}

	private List<Double> getAverageValues(int depth) {

		Position position = averagingStrategy.computeNewPositionFor(depth, 1);

		if (position.isNeutral())
			return new ArrayList<Double>();

		return averagingStrategy.getAverageValues(depth);
	}

	private List<Candle> getConstantDataForPrediction(int depth) {
		return allDataStorage.getAfter(candlesStorage.last(), depth);
	}

	private List<Candle> getVariableDataForPrediction(int depth) {

		if (candlesStorage.size() < depth)
			return new ArrayList<Candle>();

		List<Candle> history = candlesStorage.last(depth);

		double min = history.get(0).getValue();
		double max = min;

		for (Candle candle : history) {
			if (min > candle.getValue())
				min = candle.getValue();

			if (max < candle.getValue())
				max = candle.getValue();
		}

		double delta = (max - min) / history.get(0).getValue();

		Candle last = candlesStorage.last();
		int currentIndex = allDataStorage.findIndexFor(last);

		min = allDataStorage.get(currentIndex).getValue();
		max = min;

		int requiredDepth = 0;
		for (int i = currentIndex; i < allDataStorage.size(); i++) {
			requiredDepth++;
			Candle candle = allDataStorage.get(i);

			if (min > candle.getValue())
				min = candle.getValue();

			if (max < candle.getValue())
				max = candle.getValue();

			if ((max - min) / allDataStorage.get(currentIndex).getValue() >= delta)
				break;
		}

		return allDataStorage.getAfter(last, requiredDepth);
	}

	@Override
	public boolean hasCurrentState() {
		return averagingStrategy.hasCurrentState();
	}
}
