package trade.core.fourierConstructors;

import org.apache.commons.math3.transform.TransformType;

/**
 * Created by ledenev.p on 21.04.2016.
 */
public class InverseFourierTransform extends FastFourierTransformer {
	public InverseFourierTransform(Pow2Expander expander) {
		super(expander);
	}

	protected TransformType getType() {
		return TransformType.INVERSE;
	}
}
