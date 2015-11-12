package model;

import lombok.*;
import tools.*;

import java.util.*;

/**
 * Created by DiKey on 11.08.2015.
 */

@Data
@AllArgsConstructor
public class TrainingResult {

    List<Double> inputs;
    Direction direction;

    public static TrainingResult empty() {
        return createForCandles(new ArrayList<Candle>(), Direction.neutral);
    }

    public static TrainingResult createForCandles(List<Candle> candles) {
        return createForCandles(candles, Direction.neutral);
    }

    public static TrainingResult createFor(List<Double> values, Direction direction) {

        List<Double> valueIncrements = new ArrayList<Double>();
        for (int i = 0; i < values.size() - 1; i++) {
            double increment = (values.get(i + 1) - values.get(i)) / values.get(i);
            valueIncrements.add(increment);
        }

        if (valueIncrements.size() > 1) {
            valueIncrements = normalize(valueIncrements);
            valueIncrements = centralize(valueIncrements);
            valueIncrements = normalize(valueIncrements);
        }

        return new TrainingResult(round(valueIncrements), direction);
    }

    public static TrainingResult createWithParams(Direction direction, Double... params) {
        List<Double> inputs = Arrays.asList(params);

        inputs = normalize(inputs);
        inputs = centralize(inputs);
        inputs = normalize(inputs);

        return new TrainingResult(round(inputs), direction);
    }

    public static TrainingResult createForCandles(List<Candle> candles, Direction direction) {

        List<Double> values = new ArrayList<Double>();
        for (Candle candle : candles)
            values.add(candle.getValue());

        return createFor(values, direction);
    }

    public static List<Double> normalize(List<Double> values) {

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (Double each : values) {
            max = (each > max) ? each : max;
            min = (each < min) ? each : min;
        }

        double minPeriod = -0.8;
        double maxPeriod = 0.8;

        List<Double> normalizedValues = new ArrayList<Double>();
        for (Double each : values) {
            double normalizedValue = (each - min) * (maxPeriod - minPeriod) / (max - min) + minPeriod;
            normalizedValues.add(normalizedValue);
        }

        return normalizedValues;
    }

    public static double mean(List<Candle> candles) {

        double mean = 0;
        for (Candle each : candles)
            mean += each.getValue();

        return mean / candles.size();
    }

    public static double max(List<Candle> values) {

        double max = Double.MIN_VALUE;
        for (Candle each : values)
            if (max < each.getValue())
                max = each.getValue();

        return max;
    }

    public static double min(List<Candle> values) {

        double min = Double.MAX_VALUE;
        for (Candle each : values)
            if (min > each.getValue())
                min = each.getValue();

        return min;
    }

    public static double sigma(List<Candle> candles) {

        double mean = mean(candles);
        double mean2 = 0;

        for (Candle each : candles)
            mean2 += Math.pow(each.getValue(), 2);

        return Math.pow(mean2 / (double) candles.size() - mean * mean, 0.5);
    }

    public static List<Double> centralize(List<Double> values) {

        double mean = 0;
        for (Double each : values)
            mean += each;
        mean = mean / values.size();

        double sigma = 0;
        for (Double each : values)
            sigma += Math.pow(each - mean, 2);
        sigma = Math.pow(sigma / (double) values.size(), 0.5);

        List<Double> centralizedValues = new ArrayList<Double>();
        for (Double each : values)
            centralizedValues.add((each - mean) / sigma);

        return centralizedValues;
    }

    public static List<Double> round(List<Double> values) {
        List<Double> roundedValues = new ArrayList<Double>();

        for (Double each : values) {
            try {
                roundedValues.add(Round.toSignificant(each));
            } catch (Throwable e) {
                Log.error(e.getMessage());
            }
        }

        return roundedValues;
    }

    public String print() {
        String result = "";

        for (double value : inputs)
            result += value + ";";

        String signal = "0;0;1";

        if (Direction.buy.equals(direction))
            signal = "1;0;0";

        if (Direction.sell.equals(direction))
            signal = "0;1;0";

        return result + signal;
    }

    public boolean hasSameIncrements(TrainingResult result) {

        if (inputs.size() != result.getInputs().size())
            return false;

        for (int i = 0; i < inputs.size(); i++)
            if (inputs.get(i) != result.getInputs().get(i))
                return false;

        return true;
    }

    public double[] getNormalizedValueIncrementsAsArray() {

        double[] result = new double[inputs.size()];

        int i = 0;
        for (Double item : inputs)
            result[i++] = item;

        return result;
    }

    public int incrementsLength() {
        return inputs.size();
    }

    public boolean isHold() {
        return direction.equals(Direction.hold);
    }

    public boolean isNeutral() {
        return direction.equals(Direction.neutral);
    }
}
