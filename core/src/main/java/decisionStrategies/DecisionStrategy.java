package decisionStrategies;

import exceptions.*;
import lombok.*;
import model.*;
import org.reflections.*;
import takeProfitStrategies.*;
import tools.*;

import java.util.*;

/**
 * Created by ledenev.p on 09.04.2015.
 */
@Data
public abstract class DecisionStrategy {

    protected CandlesStorage candlesStorage;
    private ITakeProfitStrategy profitStrategy;

    public static DecisionStrategy createFor(String name, ITakeProfitStrategy profitStrategy, CandlesStorage candlesStorage)
            throws Throwable {
        Reflections reflections = new Reflections("decisionStrategies");
        Set<Class<?>> strategyClasses = reflections.getTypesAnnotatedWith(Strategy.class);

        if (strategyClasses.size() <= 0)
            throw new NoDecisionStrategyFoundFailure("no strategy class found");

        for (Class strategyClass : strategyClasses) {
            String strategyName = ((Strategy) strategyClass.getAnnotation(Strategy.class)).name();

            if (DecisionStrategy.class.isAssignableFrom(strategyClass) && strategyName.equals(name)) {
                DecisionStrategy strategy = (DecisionStrategy) strategyClass.newInstance();
                strategy.setProfitStrategy(profitStrategy);
                strategy.setCandlesStorage(candlesStorage);

                return strategy;
            }
        }

        throw new NoDecisionStrategyFoundFailure("for name " + name);
    }

    public DecisionStrategy() {
    }

    public DecisionStrategy(ITakeProfitStrategy profitStrategy, CandlesStorage candlesStorage) {
        this();

        this.profitStrategy = profitStrategy;
        this.candlesStorage = candlesStorage;
    }

    public Position computeNewPositionFor(int depth, int volume) {

        if (candlesStorage.lessThan(getInitialStorageSizeFor(depth)))
            Log.debug("CandlesStorage size less than initial size: " + getInitialStorageSizeFor(depth));

        if (profitStrategy.shouldTakeProfit())
            return Position.closing(candlesStorage.last());

        Direction direction = computeOrderDirection(depth);

        return Position.opening(direction, volume, candlesStorage.last());
    }

    protected abstract Direction computeOrderDirection(int depth);

    public String getName() {
        Strategy annotation = this.getClass().getAnnotation(Strategy.class);

        return annotation.name();
    }

    protected Candle[] createCandleArrayBy(int start, int depth) {

        Candle[] array = new Candle[depth];

        for (int i = 0; i < depth; i++)
            array[i] = candlesStorage.get(start - depth + i + 1);

        return array;
    }

    public StrategyState getCurrentState() {
        return new StrategyState(candlesStorage.last(), collectCurrentStateParams());
    }

    public abstract String[] getStateParamsHeader();

    protected abstract String[] collectCurrentStateParams();

    public abstract int getInitialStorageSizeFor(int depth);

    public Candle getLastCandle() {
        return candlesStorage.last();
    }
}