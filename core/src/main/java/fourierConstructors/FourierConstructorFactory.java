package fourierConstructors;

/**
 * Created by DiKey on 25.04.2015.
 */
public class FourierConstructorFactory {

	public static IFourierConstructor createConstructor(int frequenciesNumber) {
		return new FourierTransformConstructor(frequenciesNumber);
	}
}
