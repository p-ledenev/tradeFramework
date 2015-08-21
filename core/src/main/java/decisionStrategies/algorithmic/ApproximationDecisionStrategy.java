package decisionStrategies.algorithmic;

import approximationConstructors.*;
import decisionStrategies.*;
import lombok.*;
import model.*;

/**
 * Created by ledenev.p on 05.05.2015.
 */

@Data
@Strategy(name = "ApproximationStrategy")
public class ApproximationDecisionStrategy extends DecisionStrategy {

    private IApproximationConstructor constructor;
    private Approximation ap;

    public ApproximationDecisionStrategy() {
        constructor = new LinearApproximationConstructor();
    }

    @Override
    protected Direction computeOrderDirection(Candle[] candles) {

        if (candlesStorage.size() < candles.length)
            return Direction.neutral;

        ap = constructor.approximate(candles);

        double highestDegreeParam = ap.getHighestDegreeParameter();

        if (highestDegreeParam > 0)
            return Direction.buy;

        if (highestDegreeParam < 0)
            return Direction.sell;

        return Direction.neutral;
    }

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                "k", "kx+b", "approximatedValue"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString(ap.getHighestDegreeParameter()),
                ap.printPowerFunction(),
                Double.toString(ap.computeApproximatedValue())
        };
    }

    @Override
    public int getInitialStorageSizeFor(int depth) {
        return depth + 2;
    }
}
