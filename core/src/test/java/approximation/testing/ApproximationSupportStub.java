package approximation.testing;

import approximationConstructors.IApproximationSupport;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class ApproximationSupportStub implements IApproximationSupport {

    private double value;

    public ApproximationSupportStub(double value) {
        this.value = value;
    }

    @Override
    public double getValueForApproximation() {
        return value;
    }
}
