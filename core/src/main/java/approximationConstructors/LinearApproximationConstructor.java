package approximationConstructors;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import tools.Round;

/**
 * Created by ledenev.p on 17.04.2015.
 */
public class LinearApproximationConstructor implements IApproximationConstructor {

    @Override
    public Approximation approximate(IApproximationSupport[] values) {

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[] data = createOLSDataFrom(values);

        regression.newSampleData(data, values.length, 1);
        double[] regressionParameters = regression.estimateRegressionParameters();

        return new Approximation(round(regressionParameters));
    }

    private double[] extractYValues(IApproximationSupport[] values) {
        double[] y = new double[values.length];

        int i = 0;
        for (IApproximationSupport value : values)
            y[i++] = value.getValueForApproximation();

        return y;
    }

    private double[] createOLSDataFrom(IApproximationSupport[] values) {
        double[] data = new double[2 * values.length];

        int i = 0;
        for (int k = 0; k < values.length; k++) {
            data[i++] = values[k].getValueForApproximation();
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
