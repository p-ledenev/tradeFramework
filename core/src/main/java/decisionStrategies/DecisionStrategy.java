package decisionStrategies;

import exceptions.NoDecisionStrategyFoundFailure;
import lombok.Data;
import model.Candle;
import model.OrderDirection;
import model.Position;
import org.joda.time.DateTime;
import org.reflections.Reflections;
import siftStrategies.ISiftCandlesStrategy;
import takeProfitStrategies.ITakeProfitStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ledenev.p on 09.04.2015.
 */
@Data
public abstract class DecisionStrategy {

    protected List<Candle> candles;
    private ITakeProfitStrategy profitStrategy;
    private ISiftCandlesStrategy siftStrategy;

    public static DecisionStrategy createFor(String name, ITakeProfitStrategy profitStrategy, ISiftCandlesStrategy siftStrategy)
            throws Throwable {
        Reflections reflections = new Reflections("decisionStrategies");
        Set<Class<?>> strategyClasses = reflections.getTypesAnnotatedWith(Strategy.class);

        if (strategyClasses.size() <= 0)
            throw new NoDecisionStrategyFoundFailure("no strategy classes found");

        for (Class strategyClass : strategyClasses) {
            String strategyName = ((Strategy) strategyClass.getAnnotation(Strategy.class)).name();

            if (DecisionStrategy.class.isAssignableFrom(strategyClass) && strategyName.equals(name)) {
                DecisionStrategy strategy = (DecisionStrategy) strategyClass.newInstance();
                strategy.setProfitStrategy(profitStrategy);
                strategy.setSiftStrategy(siftStrategy);

                return strategy;
            }
        }

        throw new NoDecisionStrategyFoundFailure("for name " + name);
    }

    public DecisionStrategy() {
        candles = new ArrayList<Candle>();
    }

    public DecisionStrategy(ITakeProfitStrategy profitStrategy, ISiftCandlesStrategy siftStrategy) {
        this();

        this.profitStrategy = profitStrategy;
        this.siftStrategy = siftStrategy;
    }

    public Position computeNewPositionFor(List<Candle> newCandles, int depth, int volume) {

        List<Candle> sifted = siftStrategy.sift(getLastCandle(), newCandles);
        candles.addAll(sifted);

        if (candles.size() < depth)
            return Position.closing(getLastCandleDate());

        if (profitStrategy.shouldTakeProfit())
            return Position.closing(getLastCandleDate());

        OrderDirection direction = computeOrderDirection(depth);

        return Position.opening(direction, volume, getLastCandleDate());
    }

    public DateTime getLastCandleDate() {
        return getLastCandle().getDate();
    }

    public Candle getLastCandle() {

        if (candles.size() == 0)
            return Candle.empty();

        return candles.get(candles.size() - 1);
    }

    protected abstract OrderDirection computeOrderDirection(int depth);

    public String getName() {
        Strategy annotation = this.getClass().getAnnotation(Strategy.class);

        return annotation.name();
    }

    protected Candle[] createCandleArrayBy(int start, int depth) {

        Candle[] array = new Candle[depth];

        for (int i = 0; i < depth; i++)
            array[i] = candles.get(start - depth + i + 1);

        return array;
    }

    public StrategyState getCurrentState() {
        return new StrategyState(getLastCandle(), collectCurrentStateParams());
    }

    public abstract String[] getStateParamsHeader();

    protected abstract String[] collectCurrentStateParams();
}