package trade.researcher.tryOut.run;

/**
 * Created by ledenev.p on 08.04.2016.
 */
public class SieveParamsReader extends ResearcherParamsReader {

	public static ResearcherParamsReader read(String file) throws Throwable {
		return new SieveParamsReader(file);
	}

	private SieveParamsReader(String file) throws Throwable {
		super(file);
	}

	@Override
	protected int getLineNumber() {
		return 0;
	}
}
