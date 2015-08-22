package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import tools.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 11.08.2015.
 */

@Data
@AllArgsConstructor
public class TrainingResult {

    List<Double> normalizedValueIncrements;
    Direction direction;

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

        double borderMin = 0;
        double borderMax = 1;

        List<Double> normalizeValueIncrements = new ArrayList<Double>();
        for (Double each : valueIncrements) {
            double normalizedValue = (each - min) * (borderMax - borderMin) / (max - min) + borderMin;
            normalizeValueIncrements.add(Round.toSignificant(normalizedValue));
        }

        return new TrainingResult(normalizeValueIncrements, direction);
    }

    public String print() {
        String result = "";

        for (double value : normalizedValueIncrements)
            result += value + ";";

        String tutorDecision = "0;0";
        if (Direction.buy.equals(direction))
            tutorDecision = "1;0";

        if (Direction.sell.equals(direction))
            tutorDecision = "0;1";

        return result + tutorDecision;
    }

    public boolean hasSameIncrements(TrainingResult result) {

        if (normalizedValueIncrements.size() != result.getNormalizedValueIncrements().size())
            return false;

        for (int i = 0; i < normalizedValueIncrements.size(); i++)
            if (normalizedValueIncrements.get(i) != result.getNormalizedValueIncrements().get(i))
                return false;

        return true;
    }
}
