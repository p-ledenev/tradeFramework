package trade.core.fourierConstructors;

import org.apache.commons.math3.transform.TransformType;

/**
 * Created by ledenev.p on 21.04.2016.
 */
public class DirectFourierTransform extends FastFourierTransformer {
	public DirectFourierTransform(Pow2Expander expander) {
		super(expander);
	}

	protected TransformType getType() {
		return TransformType.FORWARD;
	}
}
