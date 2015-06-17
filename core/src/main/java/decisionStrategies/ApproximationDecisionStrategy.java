package decisionStrategies;

import approximationConstructors.Approximation;
import approximationConstructors.IApproximationConstructor;
import approximationConstructors.LinearApproximationConstructor;
import lombok.Data;
import model.Direction;

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
    protected Direction computeOrderDirection(int depth) {

        ap = constructor.approximate(createCandleArrayBy(candles.size() - 1, depth));

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
    public int getSufficientCandlesSizeFor(int depth) {
        return depth + 2;
    }
}
