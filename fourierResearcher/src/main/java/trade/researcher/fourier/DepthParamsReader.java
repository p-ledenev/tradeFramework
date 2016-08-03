package trade.researcher.fourier;

import trade.researcher.tryOut.run.ResearcherParamsReader;

/**
 * Created by ledenev.p on 08.04.2016.
 */
public class DepthParamsReader extends ResearcherParamsReader {

	public static ResearcherParamsReader read(String file) throws Throwable {
		return new DepthParamsReader(file);
	}

	private DepthParamsReader(String file) throws Throwable {
		super(file);
	}

	@Override
	protected int getLineNumber() {
		return 0;
	}
}
