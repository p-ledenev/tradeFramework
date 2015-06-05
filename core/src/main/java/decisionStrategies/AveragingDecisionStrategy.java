package decisionStrategies;

import averageConstructors.AverageConstructorFactory;
import averageConstructors.IAverageConstructor;
import averageConstructors.IAveragingSupport;
import lombok.Data;
import model.OrderDirection;
import tools.Format;
import tools.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
@Strategy(name = "AveragingStrategy")
public class AveragingDecisionStrategy extends DecisionStrategy {

    private List<Double> averageValues;
    private List<Derivative> derivatives;
    private List<Double> averageDerivatives;

    private IAverageConstructor constructor;

    public AveragingDecisionStrategy() {
        averageValues = new ArrayList<Double>();
        derivatives = new ArrayList<Derivative>();
        averageDerivatives = new ArrayList<Double>();

        constructor = AverageConstructorFactory.createConstructor();
    }

    @Override
    protected OrderDirection computeOrderDirection(int depth) {

        addDerivatives(depth);

        double averageDerivative = getLastAverageDerivative();

        if (averageDerivative > 0)
            return OrderDirection.buy;

        if (averageDerivative < 0)
            return OrderDirection.sell;

        return OrderDirection.none;
    }

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                "average", "derivative", "averageDerivative"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString((int) getLastAverageValue()),
                Double.toString(Round.toSignificant(getLastDerivativeValue() * 10000)),
                Double.toString(Round.toSignificant(getLastAverageDerivative() * 100000))
        };
    }

    private void addDerivatives(int depth) {

        int start = (derivatives.size() + 2 > depth - 1) ? derivatives.size() + 2 : depth - 1;
        for (int i = start; i < candles.size(); i++) {

            double averageValue = constructor.average(createCandleArrayBy(i, depth));
            double derivativeValue = (averageValue - getLastAverageValue()) / averageValue;

            averageValues.add(averageValue);
            derivatives.add(new Derivative(derivativeValue));

            double averageDerivative = 0;
            if (derivatives.size() >= depth) {
                List<Derivative> derivativesToAverage = Format.copyFromEnd(derivatives, depth);
                averageDerivative = constructor.average(asArray(derivativesToAverage));
            }
            averageDerivatives.add(averageDerivative);
        }

        int k = 0;
    }

    private IAveragingSupport[] asArray(List<Derivative> derivatives) {
        return derivatives.toArray(new Derivative[derivatives.size()]);
    }

    private double getLastAverageValue() {

        if (averageValues.size() == 0)
            return 0.;

        return averageValues.get(averageValues.size() - 1);
    }

    private double getLastAverageDerivative() {
        return averageDerivatives.get(averageDerivatives.size() - 1);
    }

    private double getLastDerivativeValue() {

        if (derivatives.size() == 0)
            return 0.;

        return derivatives.get(derivatives.size() - 1).getValue();
    }
}
