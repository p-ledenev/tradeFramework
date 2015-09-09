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

    List<Double> normalizedValueIncrements;
    Direction direction;

    public static TrainingResult createFor(List<Candle> candles) {
        return createFor(candles, Direction.neutral);
    }

    public static TrainingResult createFor(List<Candle> candles, Direction direction) {

        List<Double> valueIncrements = new ArrayList<Double>();
        for (int i = 0; i < candles.size() - 1; i++) {
            double increment = (candles.get(i + 1).getValue() - candles.get(i).getValue()) / candles.get(i).getValue();
            valueIncrements.add(increment);
        }

        valueIncrements = normalize(valueIncrements);
        valueIncrements = centralize(valueIncrements);
        valueIncrements = normalize(valueIncrements);

        return new TrainingResult(round(valueIncrements), direction);
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

        for (Double each : values)
            roundedValues.add(Round.toSignificant(each));

        return roundedValues;
    }

    public String print() {
        String result = "";

        for (double value : normalizedValueIncrements)
            result += value + ";";

        String signal = "0;0;1";

        if (Direction.buy.equals(direction))
            signal = "1;0;0";

        if (Direction.sell.equals(direction))
            signal = "0;1;0";

        return result + signal;
    }

    public boolean hasSameIncrements(TrainingResult result) {

        if (normalizedValueIncrements.size() != result.getNormalizedValueIncrements().size())
            return false;

        for (int i = 0; i < normalizedValueIncrements.size(); i++)
            if (normalizedValueIncrements.get(i) != result.getNormalizedValueIncrements().get(i))
                return false;

        return true;
    }

    public double[] getNormalizedValueIncrementsAsArray() {

        double[] result = new double[normalizedValueIncrements.size()];

        int i = 0;
        for (Double item : normalizedValueIncrements)
            result[i++] = item;

        return result;
    }
}
