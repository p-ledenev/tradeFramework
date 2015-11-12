package decisionStrategies.algorithmic;

import averageConstructors.*;
import decisionStrategies.*;
import lombok.*;
import model.*;
import tools.*;

import java.util.*;

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
    protected Direction computeOrderDirection(Candle[] candles) {

        addDerivatives(candles);

        double averageDerivative = getLastAverageDerivative();

        if (averageDerivative > 0)
            return Direction.buy;

        if (averageDerivative < 0)
            return Direction.sell;

        return Direction.neutral;
    }

    @Override
    public boolean couldOpenPosition(int depth, Position previousPosition) {
        return Math.abs(getLastAverageDerivative()) * 100000 > depth * 0.05;
    }

    @Override
    public String[] getStateParamsHeader() {
        return new String[]{
                // "average", "derivative", "averageDerivative"
                "average", "averageDerivative"
        };
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[]{
                Double.toString((int) getLastAverageValue()),
                //Double.toString(Round.toSignificant(getLastDerivativeValue() * 10000)),
                Double.toString(Round.toSignificant(getLastAverageDerivative() * 100000))
        };
    }

    @Override
    public int getInitialStorageSizeFor(int depth) {
        return depth * 2 + 3;
    }

    private void addDerivatives(Candle[] candles) {

        int depth = candles.length;

        int start = (derivatives.size() + 2 > depth - 1) ? derivatives.size() + 2 : depth - 1;
        for (int i = start; i < candlesStorage.size(); i++) {

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
    }

    private IAveragingSupport[] asArray(List<Derivative> derivatives) {
        return derivatives.toArray(new Derivative[derivatives.size()]);
    }

    public double getLastAverageValue() {

        if (averageValues.size() == 0)
            return 0;

        return averageValues.get(averageValues.size() - 1);
    }

    public double getLastAverageDerivative() {

        if (averageDerivatives.size() == 0)
            return 0;

        return averageDerivatives.get(averageDerivatives.size() - 1);
    }

    private double getLastDerivativeValue() {

        if (derivatives.size() == 0)
            return 0.;

        return derivatives.get(derivatives.size() - 1).getValue();
    }

    public List<Double> getAverageValues(int depth) {

        int size = averageValues.size();

        List<Double> result = new ArrayList<Double>();
        if (size < depth)
            return result;

        for (int i = 0; i < depth; i++)
            result.add(averageValues.get(size - depth + i));

        return result;
    }

    @Override
    public boolean hasCurrentState() {
        return averageDerivatives.size() != 0;
    }
}