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

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        List<Double> valueIncrements = new ArrayList<Double>();
        for (int i = 0; i < candles.size() - 1; i++) {
            double increment = (candles.get(i + 1).getValue() - candles.get(i).getValue()) / candles.get(i).getValue();
            valueIncrements.add(increment);

            max = (increment > max) ? increment : max;
            min = (increment < min) ? increment : min;
        }

        double minPeriod = 0;
        double maxPeriod = 1;

        List<Double> normalizeValueIncrements = new ArrayList<Double>();
        for (Double each : valueIncrements) {

            double normalizedValue = (each - min) * (maxPeriod - minPeriod) / (max - min) + minPeriod;
            normalizeValueIncrements.add(Round.toSignificant(normalizedValue));
        }

        return new TrainingResult(normalizeValueIncrements, direction);
    }

    public String print() {
        String result = "";

        for (double value : normalizedValueIncrements)
            result += value + ";";

        return result + direction.getSign();
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
