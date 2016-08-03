package trade.core.decisionStrategy.testing;

import org.joda.time.DateTime;
import org.junit.Test;
import trade.core.averageConstructors.ExponentialAverageConstructor;
import trade.core.decisionStrategies.algorithmic.AveragingDecisionStrategy;
import trade.core.model.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by ledenev.p on 28.04.2015.
 */
public class AverageDecisionStrategyTest extends DecisionStrategyTestCase<AveragingDecisionStrategy> {

	@Test
	public void shouldComputeOrderDirection() {
		Position position = decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(position.getDirection(), is(equalTo(Direction.Buy)));
		assertThat(position.getVolume(), is(equalTo(volume)));
	}

	@Test
	public void shouldComputeDerivative() {
		decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
	}

	@Test
	public void shouldComputeAverageDerivative() {
		decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(getLastAverageDerivative(), is(equalTo(0.1343080830363829)));
	}

	@Test
	public void examineArraysLength() {
		decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(decisionStrategy.getCandlesStorage().size(), is(equalTo(candles.size())));
		assertThat(decisionStrategy.getAverageValues().size(), is(equalTo(candles.size() - depth + 1)));
		assertThat(decisionStrategy.getDerivatives().size(), is(equalTo(candles.size() - depth + 1)));
		assertThat(decisionStrategy.getAverageDerivatives().size(), is(equalTo(candles.size() - depth + 1)));
	}

	@Test
	public void shouldAddOneNewCandle() {
		decisionStrategy.computeNewPositionFor(depth, volume);

		List<Candle> newCandles = new ArrayList<Candle>();
		newCandles.add(new Candle(DateTime.now(), 5.5));

		decisionStrategy.getCandlesStorage().add(newCandles);
		decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(decisionStrategy.getCandlesStorage().size(), is(equalTo(candles.size() + 1)));
		assertThat(decisionStrategy.getAverageValues().size(), is(equalTo(candles.size() + 1 - depth + 1)));
		assertThat(decisionStrategy.getDerivatives().size(), is(equalTo(candles.size() + 1 - depth + 1)));
		assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
		assertThat(getLastAverageDerivative(), is(equalTo(0.08408860803423382)));
	}

	@Test
	public void shouldAddTwoNewCandles() {
		List<Candle> newCandles = new ArrayList<Candle>();
		newCandles.add(new Candle(DateTime.now(), 5.5));
		newCandles.add(new Candle(DateTime.now(), 5.1));

		decisionStrategy.getCandlesStorage().add(newCandles);
		decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(decisionStrategy.getDerivatives().size(), is(equalTo(candles.size() + 2 - depth + 1)));
		assertThat(getLastDerivative(), is(equalTo(computeLastDerivative())));
		assertThat(getLastAverageDerivative(), is(equalTo(-0.04567186553941528)));
	}

	@Test
	public void shouldReturnNoneDirectionForMinCandlesLength() {

		decisionStrategy.getCandlesStorage().setCandles(new ArrayList<Candle>());

		List<Candle> newCandles = new ArrayList<Candle>();
		newCandles.add(new Candle(DateTime.now(), 5.5));
		newCandles.add(new Candle(DateTime.now(), 4.5));
		newCandles.add(new Candle(DateTime.now(), 6.0));

		decisionStrategy.getCandlesStorage().add(newCandles);
		Position position = decisionStrategy.computeNewPositionFor(depth, volume);

		assertThat(position.getDirection(), is(equalTo(Direction.Neutral)));
	}

	@Override
	protected AveragingDecisionStrategy createStrategy() {
		AveragingDecisionStrategy decisionStrategy = new AveragingDecisionStrategy();
		decisionStrategy.setConstructor(new ExponentialAverageConstructor());

		return decisionStrategy;
	}

	private double getLastDerivative() {
		return decisionStrategy.getDerivatives().get(decisionStrategy.getDerivatives().size() - 1);
	}

	private double computeLastDerivative() {
		double averageValue1 = decisionStrategy.getAverageValues().get(decisionStrategy.getAverageValues().size() - 1);
		double averageValue2 = decisionStrategy.getAverageValues().get(decisionStrategy.getAverageValues().size() - 2);

		return (averageValue1 - averageValue2) / averageValue1;
	}

	public double getLastAverageDerivative() {
		return decisionStrategy.getAverageDerivatives().get(decisionStrategy.getAverageDerivatives().size() - 1);
	}
}
