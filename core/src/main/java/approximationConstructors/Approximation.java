package approximationConstructors;

import lombok.*;
import tools.*;

/**
 * Created by ledenev.p on 17.04.2015.
 */

@Data
public class Approximation {

    private double[] regressionParameters;
    private int xValue;

    public Approximation(double[] regressionParameters, int xValue) {
        this.regressionParameters = regressionParameters;
        this.xValue = xValue;
    }

    public double getHighestDegreeParameter() {
        return regressionParameters[regressionParameters.length - 1];
    }

    public String printPowerFunction() {

        String response = "";
        if (regressionParameters == null)
            return response;

        int maxPower = regressionParameters.length;
        for (int i = maxPower - 1; i >= 0; i--) {
            double k = Round.toMoneyAmount(regressionParameters[i]);

            response += (k > 0 ? "+" : "") + k + "x" + i;
        }
        return response;
    }

    public double computeApproximatedValue() {

        double value = 0;
        if (regressionParameters == null)
            return value;

        for (int i = regressionParameters.length - 1; i >= 0; i--)
            value += regressionParameters[i] * Math.pow(xValue - 1, i);

        return Round.toMoneyAmount(value);
    }


}
