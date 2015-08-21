package decisionStrategies.neuron;

import decisionStrategies.*;
import lombok.*;
import model.*;

import java.util.*;

/**
 * Created by DiKey on 08.08.2015.
 */
public abstract class NeuronTrainingDecisionStrategy extends DecisionStrategy {

    @Setter
    protected CandlesStorage allDataStorage;

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        TrainingResult result = computeTrainingResult(candles.length);
        return result.getDirection();
    }

    public TrainingResult computeTrainingResult(int depth) {

        Candle last = candlesStorage.last();

        List<Candle> data = allDataStorage.getAfter(last, depth);

        Direction direction;
        if (data.size() < depth)
            direction = Direction.neutral;
        else
            direction = computeDirection(data);

        return TrainingResult.createFor(data, direction);
    }

    protected abstract Direction computeDirection(List<Candle> candles);

    @Override
    public int getInitialStorageSizeFor(int depth) {
        return depth;
    }
}
