package averaging.testing;

import averageConstructors.IAveragingSupport;

/**
 * Created by DiKey on 26.04.2015.
 */
public class AveragingSupportStub implements IAveragingSupport {

    private double value;

    public AveragingSupportStub(double value) {
        this.value = value;
    }

    @Override
    public double getValueForAveraging() {
        return value;
    }
}
