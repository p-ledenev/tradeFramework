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

    List<Double> normalizeIncrements;
    List<Double> rowIncrements;
    Direction direction;

    public TrainingResult() {
        direction = Direction.Neutral;
        normalizeIncrements = new ArrayList<Double>();
        rowIncrements = new ArrayList<Double>();
    }

    public static TrainingResult empty() {
        return createForCandles(new ArrayList<Candle>(), Direction.Neutral);
    }

    public static TrainingResult createForCandles(List<Candle> candles) {
        return createForCandles(candles, Direction.Neutral);
    }

    public static TrainingResult createFor(List<Double> values, Direction direction) {

        List<Double> rowIncrements = new ArrayList<Double>();
        for (int i = 0; i < values.size() - 1; i++) {
            double increment = (values.get(i + 1) - values.get(i)) / values.get(i);
            rowIncrements.add(increment * 100);
        }

        List<Double> normalizeIncrements = new ArrayList<Double>();
        if (rowIncrements.size() > 1) {
            normalizeIncrements = centralizeWithTanh(rowIncrements);
            //normalizeIncrements = normalize(rowIncrements);
        }

        return new TrainingResult(round(normalizeIncrements), rowIncrements, direction);
    }

    public static TrainingResult createWithParams(Direction direction, Double... params) {

        List<Double> rowIncrements = Arrays.asList(params);

        List<Double> normalizeIncrements = centralizeWithTanh(rowIncrements);

        return new TrainingResult(round(normalizeIncrements), rowIncrements, direction);
    }

    public static TrainingResult createForCandles(List<Candle> candles, Direction direction) {

        List<Double> values = new ArrayList<Double>();
        for (Candle candle : candles)
            values.add(candle.getValue());

        return createFor(values, direction);
    }

    public static List<Double> centralizeWithTanh(List<Double> values) {

        List<Double> centralized = centralize(values);

        List<Double> result = new ArrayList<Double>();
        for (Double value : centralized)
            result.add(Math.tanh(value));

        return result;
    }

    public static List<Double> normalize(List<Double> values) {
        return normalize(values, -0.8, 0.8);
    }

    public static List<Double> normalize(List<Double> values, double minPeriod, double maxPeriod) {

        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (Double each : values) {
            max = (each > max) ? each : max;
            min = (each < min) ? each : min;
        }

        List<Double> normalizedValues = new ArrayList<>();
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

        for (double value : normalizeIncrements)
            result += value + ";";

        String signal = "0;0;1";

        if (Direction.Buy.equals(direction))
            signal = "1;0;0";

        if (Direction.Sell.equals(direction))
            signal = "0;1;0";

        return result + signal;
    }

    public boolean hasSameIncrements(TrainingResult result) {

        if (normalizeIncrements.size() != result.getNormalizeIncrements().size())
            return false;

        for (int i = 0; i < normalizeIncrements.size(); i++)
            if (normalizeIncrements.get(i) != result.getNormalizeIncrements().get(i))
                return false;

        return true;
    }

    public double[] getNormalizedValueIncrementsAsArray() {

        double[] result = new double[normalizeIncrements.size()];

        int i = 0;
        for (Double item : normalizeIncrements)
            result[i++] = item;

        return result;
    }

    public int incrementsLength() {
        return normalizeIncrements.size();
    }

    public boolean isHold() {
        return direction.equals(Direction.Hold);
    }

    public boolean isNeutral() {
        return direction.equals(Direction.Neutral);
    }

    public int getIntDirection() {
        if (direction.equals(Direction.Sell))
            return -1;

        if (direction.equals(Direction.Buy))
            return 1;

        return 0;
    }

    public Map<Double, Double> buildHistogram() {

        List<Double> xAxis = new ArrayList<Double>();
        int steps = 40;
        for (int i = 0; i < steps + 1; i++)
            xAxis.add(-1 + 2. / steps * i);

        Map<Double, Double> result = new TreeMap();
        for (int i = 0; i < steps; i++) {
            Double key = countMean(xAxis.get(i), xAxis.get(i + 1));
            result.put(key, 0.);
        }

        for (double input : normalizeIncrements) {

            Double leftBorder = xAxis.get(0);
            Double rightBorder = xAxis.get(xAxis.size() - 1);
            for (double x : xAxis) {
                if (input > x)
                    leftBorder = x;

                if (input <= x) {
                    rightBorder = x;
                    break;
                }
            }

            if (input == -1)
                rightBorder = xAxis.get(1);

            Double inputKey = countMean(leftBorder, rightBorder);
            result.put(inputKey, result.get(inputKey) + 1.);
        }

        return result;
    }

    private Double countMean(Double x1, double x2) {
        return Round.toThree((x1 + x2) / 2.);
    }

    public int getDirectionSign() {
        return direction.getSign();
    }

    public boolean isActive() {
        return !isHold() && !isNeutral();
    }
}
