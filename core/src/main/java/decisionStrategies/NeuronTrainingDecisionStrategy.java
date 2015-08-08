package decisionStrategies;

import lombok.Setter;
import model.Candle;
import model.CandlesStorage;
import model.Direction;

import java.util.List;

/**
 * Created by DiKey on 08.08.2015.
 */
public abstract class NeuronTrainingDecisionStrategy extends DecisionStrategy {

    @Setter
    protected CandlesStorage allDataStorage;

    @Override
    protected Direction computeOrderDirection(int depth) {
        Candle last = candlesStorage.last();

        List<Candle> data = allDataStorage.getAfter(last, depth);

        if (data.size() < depth)
            return Direction.neutral;

        return computeDirection(data);
    }

    protected abstract Direction computeDirection(List<Candle> candles);

    @Override
    public int getInitialStorageSizeFor(int depth) {
        return depth;
    }
}
