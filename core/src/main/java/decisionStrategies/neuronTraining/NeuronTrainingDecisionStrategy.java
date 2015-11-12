package decisionStrategies.neuronTraining;

import approximationConstructors.*;
import decisionStrategies.*;
import decisionStrategies.algorithmic.*;
import lombok.*;
import model.*;

import java.util.*;

/**
 * Created by DiKey on 08.08.2015.
 */
public abstract class NeuronTrainingDecisionStrategy extends DecisionStrategy {

    @Setter
    protected CandlesStorage allDataStorage;

    protected Direction directionBeforeNeutral = Direction.neutral;
    protected Direction previousDirection = Direction.neutral;

    @Setter
    protected AveragingDecisionStrategy averagingStrategy;

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        TrainingResult result = computeTrainingResult(candles.length);
        return result.getDirection();
    }

    public TrainingResult computeTrainingResult(int depth) {

        //List<Candle> futureData = getVariableDataForPrediction(depth);
        List<Candle> futureData = getConstantDataForPrediction(depth);

        if (futureData.size() < depth - 1)
            return TrainingResult.empty();

        Direction direction = computeDirection(futureData);

        if (!direction.equals(Direction.neutral))
            previousDirection = direction;

        if (direction.equals(Direction.neutral))
            directionBeforeNeutral = previousDirection;

        if (directionBeforeNeutral.equals(direction))
            direction = Direction.neutral;

        //return TrainingResult.createFor(getAverageValues(depth), direction);
        //return TrainingResult.createStatisticalForCandles(candlesStorage.last(depth), direction);

        List<Candle> pastData = candlesStorage.last(depth);
        double last = candlesStorage.last().getValue();

        averagingStrategy.computeNewPositionFor(depth, 1);
        if (averagingStrategy.getAverageDerivatives().size() == 0)
            return TrainingResult.empty();

        Approximation approximation = ApproximationConstructorFactory.createConstructor().approximate(pastData.toArray(new Candle[pastData.size()]));

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
