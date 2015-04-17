package tools;

import java.math.BigDecimal;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class Round {

    public static int toCents(double value) {
        return (int) Math.round(toMoneyAmount(value) * 100.0);
    }

    public static double toMoneyAmount(double value) {
        return toAmount(value, 2);
    }

    public static double toSignificant(double value) {
        return toAmount(value, 4);
    }

    public static double toAmount(double value, int decimalDigits) {
        return new BigDecimal(String.valueOf(value)).setScale(decimalDigits, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double upToInteger(double value, int scale) {
        long nearest = Math.round(Math.ceil(value)) / scale * scale;

        if (value > nearest)
            return nearest + scale;

        return nearest;

    }

    public static double moneyPercentFrom(double amount, double percents) {

        BigDecimal scaledAmount = new BigDecimal(amount).setScale(10, BigDecimal.ROUND_HALF_UP);
        BigDecimal scaledPercent = new BigDecimal(percents).setScale(10, BigDecimal.ROUND_HALF_UP);
        BigDecimal scaled100 = new BigDecimal(100);
        return scaledAmount.multiply(scaledPercent).divide(scaled100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
