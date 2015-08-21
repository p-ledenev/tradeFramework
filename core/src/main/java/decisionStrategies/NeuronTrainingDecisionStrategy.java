package decisionStrategies;

import lombok.Setter;
import model.Candle;
import model.CandlesStorage;
import model.Direction;
import model.TrainingResult;

import java.util.List;

/**
 * Created by DiKey on 08.08.2015.
 */
public abstract class NeuronTrainingDecisionStrategy extends DecisionStrategy {

    @Setter
    protected CandlesStorage allDataStorage;

    @Override
    protected Direction computeOrderDirection(int depth) {

        TrainingResult result = computeTrainingResult(depth);
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
