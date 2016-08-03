package trade.core.approximationConstructors;

import org.apache.commons.math3.stat.regression.*;
import trade.core.tools.*;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class LinearApproximationConstructor implements IApproximationConstructor {

	public Approximation approximate(double[] values) {

		OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
		double[] data = createOLSDataFrom(values);

		regression.newSampleData(data, values.length, 1);
		double[] regressionParameters = regression.estimateRegressionParameters();

		return new Approximation(round(regressionParameters), values.length);
	}

	private double[] createOLSDataFrom(double[] values) {
		double[] data = new double[2 * values.length];

		int i = 0;
		for (int k = 0; k < values.length; k++) {
			data[i++] = values[k];
			data[i++] = k + 1;
		}

		return data;
	}

	private double[] round(double[] params) {
		double[] rounded = new double[params.length];

		for (int i = 0; i < rounded.length; i++)
			rounded[i] = Round.toSignificant(params[i]);

		return rounded;
	}
}
