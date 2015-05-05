package decisionStrategies;

import approximationConstructors.Approximation;
import approximationConstructors.IApproximationConstructor;
import approximationConstructors.LinearApproximationConstructor;
import lombok.Data;
import model.OrderDirection;

/**
 * Created by ledenev.p on 05.05.2015.
 */

@Data
@Strategy(name ="ApproximationStrategy")
public class ApproximationDecisionStrategy extends DecisionStrategy {

    private IApproximationConstructor constructor;

    public ApproximationDecisionStrategy() {
        constructor = new LinearApproximationConstructor();
    }

    @Override
    protected OrderDirection computeOrderDirection(int depth) {

        Approximation ap = constructor.approximate(createCandleArrayBy(candles.size() - 1, depth));

        double highestDegreeParam = ap.getHighestDegreeParameter();

        if (highestDegreeParam > 0)
            return OrderDirection.buy;

        if (highestDegreeParam < 0)
            return OrderDirection.sell;

        return OrderDirection.none;
    }
}
