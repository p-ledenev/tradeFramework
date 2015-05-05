package approximationConstructors;

import lombok.Data;

/**
 * Created by ledenev.p on 17.04.2015.
 */

@Data
public class Approximation {

    private double[] regressionParameters;

    public Approximation(double[] regressionParameters) {
        this.regressionParameters = regressionParameters;
    }

    public double getHighestDegreeParameter() {
        return regressionParameters[regressionParameters.length - 1];
    }
}
