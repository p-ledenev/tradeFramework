package averageConstructors;

import tools.Format;
import tools.Round;

/**
 * Created by DiKey on 25.04.2015.
 */
public abstract class AverageConstructor implements IAverageConstructor {

    @Override
    public double average(IAveragingSupport[] values) {

        if (values == null || values.length == 0)
            return 0;

        return Round.toSignificant(nativeAverage(values));
    }

    protected abstract double nativeAverage(IAveragingSupport[] values);
}
