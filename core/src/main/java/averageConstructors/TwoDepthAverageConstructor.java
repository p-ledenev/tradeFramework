package averageConstructors;

import tools.Format;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class TwoDepthAverageConstructor implements IAverageConstructor {

    private IAverageConstructor averageConstructor;

    public TwoDepthAverageConstructor(IAverageConstructor averageConstructor) {
        this.averageConstructor = averageConstructor;
    }

    @Override
    public double average(IAveragingSupport[] values) {
        IAveragingSupport[] halfValues = Format.takeFromEnd(values, values.length / 2);

        double fast = averageConstructor.average(halfValues);
        double slow = averageConstructor.average(values);

        return 2 * fast - slow;
    }
}
