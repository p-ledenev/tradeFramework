package trade.core.fourierConstructors;

/**
 * Created by DiKey on 25.04.2015.
 */
public class FourierConstructorFactory {

	public static IFourierConstructor createConstructor(int frequenciesNumber) {
		IPackingValueSupplier supplier = new MeanValueSupplier();
		Pow2Expander expander = new TailPow2Expander(supplier);

		return new FourierTransformConstructor(frequenciesNumber, expander);
	}

	public static IFourierConstructor createSEMAConstructor(int fastFrequenciesNumber, int slowFrequenciesNumber) {
		IPackingValueSupplier supplier = new MeanValueSupplier();
		Pow2Expander expander = new TailPow2Expander(supplier);

		return new SEMAFourierTransformConstructor(slowFrequenciesNumber, fastFrequenciesNumber, expander);
	}

	public static IFourierConstructor createLastValueTailConstructor(int frequenciesNumber) {
		IPackingValueSupplier supplier = new LastValueSupplier();
		Pow2Expander expander = new TailPow2Expander(supplier);

		return new FourierTransformConstructor(frequenciesNumber, expander);
	}
}
