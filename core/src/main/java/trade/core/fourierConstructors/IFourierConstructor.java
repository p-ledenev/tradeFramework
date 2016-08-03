package trade.core.fourierConstructors;

import org.apache.commons.math3.complex.Complex;

/**
 * Created by DiKey on 12.04.2015.
 */
public interface IFourierConstructor {

	Complex[] transform(double[] values);
}
