package decisionStrategies;

import averageConstructors.AverageConstructorFactory;
import averageConstructors.IAverageConstructor;
import averageConstructors.IAveragingSupport;
import lombok.Data;
import model.Candle;
import model.OrderDirection;
import tools.Format;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
public class AveragingDecisionStrategy extends DecisionStrategy {

    private List<Double> averageValues;
    private List<Derivative> derivatives;
    private List<Double> averageDerivatives;

    public AveragingDecisionStrategy() {
        averageValues = new ArrayList<Double>();
        derivatives = new ArrayList<Derivative>();
        averageDerivatives = new ArrayList<Double>();
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

    private void addDerivatives(int depth) {
        IAverageConstructor constructor = AverageConstructorFactory.createConstructor();

        int start = (derivatives.size() + 2 > depth - 1) ? derivatives.size() + 2 : depth - 1;
        for (int i = start; i < candles.size(); i++) {

            double averageValue = constructor.average(createArrayForAverageBy(i, depth));
            double derivativeValue = (averageValue - getLastAverageValue()) / averageValue;

            averageValues.add(averageValue);
            derivatives.add(new Derivative(derivativeValue));

            double averageDerivative = 0;
            if (derivatives.size() >= depth)
                averageDerivative = constructor.average(Format.copyFromEnd(derivativesAsArray(), depth));

            averageDerivatives.add(averageDerivative);
        }
    }

    private IAveragingSupport[] derivativesAsArray() {
        return derivatives.toArray(new Derivative[derivatives.size()]);
    }

    private IAveragingSupport[] createArrayForAverageBy(int start, int depth) {

        Candle[] array = new Candle[depth];

        for (int i = 0; i < depth; i++)
            array[i] = candles.get(start - depth + i + 1);

        return array;
    }

    private double getLastAverageValue() {

        if (averageValues.size() == 0)
            return 0;

        return averageValues.get(averageValues.size() - 1);
    }

    private double getLastAverageDerivative() {
        return averageDerivatives.get(averageDerivatives.size() - 1);
    }
}
