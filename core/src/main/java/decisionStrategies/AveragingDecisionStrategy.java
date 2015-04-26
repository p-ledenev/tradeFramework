package decisionStrategies;

import averageConstructors.AverageConstructorFactory;
import averageConstructors.IAverageConstructor;
import averageConstructors.IAveragingSupport;
import lombok.Data;
import model.Candle;
import model.OrderDirection;

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
    }

    private void addDerivatives(int depth) {
        IAverageConstructor constructor = AverageConstructorFactory.createConstructor();

        for (int i = derivatives.size(); i < candles.size(); i++) {

            double averageValue = constructor.average(createArrayForAverageBy(i, depth));
            double derivativeValue = (averageValue - getLastAverageValue()) / averageValue;

            averageValues.add(averageValue);
            derivatives.add(new Derivative(derivativeValue));

            double averageDerivative = constructor.average(derivativesAsArray());

            averageDerivatives.add(averageDerivative);
        }
    }

    private IAveragingSupport[] derivativesAsArray() {
        return derivatives.toArray(new Derivative[derivatives.size()]);
    }

    private IAveragingSupport[] createArrayForAverageBy(int start, int depth) {

        if (depth > start)
            return new Candle[0];

        Candle[] array = new Candle[depth];

        for (int i = 0; i < depth; i++)
            array[i] = candles.get(start - depth + i + 1);

        return array;
    }

    private double getLastAverageValue() {
        return averageValues.get(averageValues.size() - 1);
    }

    private double getLastAverageDerivative() {
        return averageDerivatives.get(averageDerivatives.size() - 1);
    }
}
