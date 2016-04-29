package averageConstructors;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class SimpleAverageConstructor extends AverageConstructor {

	@Override
	protected double nativeAverage(double[] values) {

		double sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i];

		return sum / values.length;
	}
}
