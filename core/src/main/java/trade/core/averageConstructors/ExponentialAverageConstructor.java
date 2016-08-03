package trade.core.averageConstructors;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class ExponentialAverageConstructor extends AverageConstructor {

	protected double L;

	public ExponentialAverageConstructor() {
		L = 2;
	}

	public ExponentialAverageConstructor(double L) {
		this.L = L;
	}

	/**
	 * fast L*F - (L-1)*MA average
	 */
	@Override
	protected double nativeAverage(double[] values) {

		double series = 0;
		int depth = values.length;
		double sum = 0;
		for (int i = depth; i > 0; i--) {
			series += 1. / i;
			sum += values[depth - i] * (series - (L - 1) / L);
		}

		return L / depth * sum;
	}
}
