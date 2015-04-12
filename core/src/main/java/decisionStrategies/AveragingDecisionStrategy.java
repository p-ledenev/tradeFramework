package decisionStrategies;

import averageConstructors.Derivative;
import model.OrderDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */
public class AveragingDecisionStrategy extends DecisionStrategy {

    private List<Derivative> derivatives;

    public AveragingDecisionStrategy() {
        derivatives =  new ArrayList<Derivative>();
    }

    @Override
    protected OrderDirection computeOrderDirection() {
        return null;
    }
}
