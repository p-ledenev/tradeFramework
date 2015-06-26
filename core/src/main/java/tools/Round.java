package tools;

import java.math.*;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class Round {

    public static double toMoneyAmount(double value) {
        return toAmount(value, 2);
    }

    public static double toDecadeAmount(double value) {
        return toAmount(value, 1);
    }

    public static double toThree(double value) {
        return toAmount(value, 3);
    }

    public static double toSignificant(double value) {
        return toAmount(value, 4);
    }

    public static double toAmount(double value, int decimalDigits) {
        return new BigDecimal(String.valueOf(value)).setScale(decimalDigits, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
