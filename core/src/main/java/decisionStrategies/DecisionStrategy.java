package decisionStrategies;

import exceptions.NoDecisionStrategyFoundFailure;
import lombok.Setter;
import model.Candle;
import model.OrderDirection;
import model.Position;
import org.reflections.Reflections;
import siftStrategies.SiftCandlesStrategy;
import takeProfitStrategies.ITakeProfitStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public abstract class DecisionStrategy {

    private List<Candle> candles;
    @Setter
    private ITakeProfitStrategy profitStrategy;
    @Setter
    private SiftCandlesStrategy siftStrategy;

    public static DecisionStrategy createFor(String name, ITakeProfitStrategy profitStrategy, SiftCandlesStrategy siftStrategy)
            throws Throwable {
        Reflections reflections = new Reflections("decisionStrategies");
        Set<Class<?>> strategyClasses = reflections.getTypesAnnotatedWith(Strategy.class);

        if (strategyClasses.size() <= 0)
            throw new NoDecisionStrategyFoundFailure("no strategy classes found");

        for (Class strategyClass : strategyClasses) {
            String strategyName = ((Strategy) strategyClass.getAnnotation(Strategy.class)).name();

            if (strategyClass.equals(DecisionStrategy.class) && strategyName.equals(name)) {
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

    public DecisionStrategy(ITakeProfitStrategy profitStrategy, SiftCandlesStrategy siftStrategy) {
        this();

        this.profitStrategy = profitStrategy;
        this.siftStrategy = siftStrategy;
    }

    public Position computeNewPositionFor(List<Candle> newCandles, int volume) {

        List<Candle> sifted = siftStrategy.sift(getLastCandle(), newCandles);
        candles.addAll(sifted);

        OrderDirection direction = computeOrderDirection();

        if (profitStrategy.shouldTakeProfit())
            return Position.closing();

        return Position.opening(direction, volume);
    }

    public Candle getLastCandle() {
        return candles.get(candles.size() - 1);
    }

    protected abstract OrderDirection computeOrderDirection();

    public String getName() {
        Strategy annotation = this.getClass().getAnnotation(Strategy.class);

        return annotation.name();
    }
}