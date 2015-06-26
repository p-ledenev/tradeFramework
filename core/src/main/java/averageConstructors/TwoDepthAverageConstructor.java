package averageConstructors;

import tools.*;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class TwoDepthAverageConstructor extends AverageConstructor {

    private IAverageConstructor averageConstructor;

    public TwoDepthAverageConstructor(IAverageConstructor averageConstructor) {
        this.averageConstructor = averageConstructor;
    }

    @Override
    protected double nativeAverage(IAveragingSupport[] values) {

        IAveragingSupport[] halfValues = Format.copyFromEnd(values, values.length / 2);

        double fast = averageConstructor.average(halfValues);
        double slow = averageConstructor.average(values);

        return 2 * fast - slow;
    }
}
