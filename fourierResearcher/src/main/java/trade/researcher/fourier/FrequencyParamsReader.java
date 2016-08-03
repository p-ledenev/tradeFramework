package trade.researcher.fourier;

import trade.researcher.tryOut.run.ResearcherParamsReader;

/**
 * Created by ledenev.p on 08.04.2016.
 */
public class FrequencyParamsReader extends ResearcherParamsReader {

	public static ResearcherParamsReader read(String file) throws Throwable {
		return new FrequencyParamsReader(file);
	}

	private FrequencyParamsReader(String file) throws Throwable {
		super(file);
	}

	@Override
	protected int getLineNumber() {
		return 1;
	}
}
