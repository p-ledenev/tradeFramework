package averageConstructors;

/**
 * Created by ledenev.p on 16.04.2015.
 */
public class TwoDepthAverageConstructor extends AverageConstructor {

	private IAverageConstructor averageConstructor;

	public TwoDepthAverageConstructor(IAverageConstructor averageConstructor) {
		this.averageConstructor = averageConstructor;
	}

	@Override
	protected double nativeAverage(double[] values) {

		double[] halfValues = fromEnd(values, values.length / 2);

		double fast = averageConstructor.average(halfValues);
		double slow = averageConstructor.average(values);

		return 2 * fast - slow;
	}

	public double[] fromEnd(double[] array, int depth) {
		return java.util.Arrays.copyOfRange(array, array.length - depth, array.length);
	}
}
