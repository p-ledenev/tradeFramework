package approximationConstructors;

import lombok.Getter;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class Approximation {

    @Getter
    private double[] regressionParameters;

    public Approximation(double[] regressionParameters) {
        this.regressionParameters = regressionParameters;
    }
}
