package averageConstructors;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class MovingAverageConstructor implements IAverageConstructor {

    @Override
    public double average(IAveragingSupport[] values) {

        double sum = 0;
        for (int i = 0; i < values.length; i++)
            sum += values[i].getValueForAveraging();

        return sum / values.length;
    }
}
