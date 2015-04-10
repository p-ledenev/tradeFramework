package decisionStrategies;

import model.Candle;
import model.OrderDirection;
import model.Position;
import siftStrategies.SiftCandlesStrategy;
import takeProfitStrategies.ITakeProfitStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 09.04.2015.
 */
public abstract class DecisionStrategy {

    private List<Candle> candles;
    private ITakeProfitStrategy profitStrategy;
    private SiftCandlesStrategy siftStrategy;

    public DecisionStrategy(ITakeProfitStrategy profitStrategy, SiftCandlesStrategy siftStrategy) {
        this.profitStrategy = profitStrategy;
        this.siftStrategy = siftStrategy;

        candles = new ArrayList<Candle>();
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
}