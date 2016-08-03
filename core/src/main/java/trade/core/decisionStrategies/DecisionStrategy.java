package trade.core.decisionStrategies;

import lombok.*;
import org.reflections.Reflections;
import trade.core.exceptions.NoDecisionStrategyFoundFailure;
import trade.core.model.*;
import trade.core.siftStrategies.ISiftCandlesStrategy;
import trade.core.tools.Log;

import java.util.Set;

/**
 * Created by ledenev.p on 09.04.2015.
 */
@Getter @Setter
public abstract class DecisionStrategy {

	protected CandlesStorage candlesStorage;

	public static DecisionStrategy createFor(String name, CandlesStorage candlesStorage)
			throws Throwable {
		Reflections reflections = new Reflections("trade/core/decisionStrategies");
		Set<Class<?>> strategyClasses = reflections.getTypesAnnotatedWith(Strategy.class);

		if (strategyClasses.size() <= 0)
			throw new NoDecisionStrategyFoundFailure("no strategy class found");

		for (Class strategyClass : strategyClasses) {
			String strategyName = ((Strategy) strategyClass.getAnnotation(Strategy.class)).name();

			if (DecisionStrategy.class.isAssignableFrom(strategyClass) && strategyName.equals(name)) {
				DecisionStrategy strategy = (DecisionStrategy) strategyClass.newInstance();
				strategy.setCandlesStorage(candlesStorage);

				return strategy;
			}
		}

		throw new NoDecisionStrategyFoundFailure("for name " + name);
	}

	public DecisionStrategy() {
	}

	public DecisionStrategy(CandlesStorage candlesStorage) {
		this();
		this.candlesStorage = candlesStorage;
	}

	public Position computeNewPositionFor(int depth, int volume) {

		if (candlesStorage.lessThan(getInitialStorageSizeFor(depth))) {
			Log.debug("CandlesStorage size " + candlesStorage.size() +
					" less than initial size: " + getInitialStorageSizeFor(depth));
			return Position.begining();
		}

		Candle[] candles = createCandleArray(depth);
		Direction direction = computeOrderDirection(candles);

		return Position.opening(direction, volume, candlesStorage.last());
	}

	public abstract Direction computeOrderDirection(Candle[] candles);

	public String getName() {
		Strategy annotation = this.getClass().getAnnotation(Strategy.class);

		return annotation.name();
	}

	private Candle[] createCandleArray(int depth) {
		return createCandleArrayBy(candlesStorage.size() - 1, depth);
	}

	protected Candle[] createCandleArrayBy(int start, int depth) {

		Candle[] array = new Candle[depth];

		for (int i = 0; i < depth; i++)
			array[i] = candlesStorage.get(start - depth + i + 1);

		return array;
	}

	public boolean couldOpenPosition(int depth, Position previousPosition) {
		return true;
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

	public abstract boolean hasCurrentState();

	public Double getSieveParam() {
		return candlesStorage.getSieveParam();
	}

	public ISiftCandlesStrategy getSiftStrategy() {
		return candlesStorage.getSiftStrategy();
	}

	public Integer getFillingGapsNumber() {
		return candlesStorage.getFillingGapsNumber();
	}

	public String printDescription() {
		return getName() + " " + getSieveParam() + " " + getFillingGapsNumber();
	}
}