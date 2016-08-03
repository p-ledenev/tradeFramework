package trade.researcher.tryOut.run;

import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

/**
 * Created by ledenev.p on 08.04.2016.
 */

@Getter
public abstract class ResearcherParamsReader {

	private double start;
	private double last;
	private int amount;

	protected ResearcherParamsReader(String file) throws Throwable {
		List<String> lines = FileUtils.readLines(new File(file));

		String[] params = lines.get(getLineNumber()).split(";");

		start = Double.parseDouble(params[0]);
		last = Double.parseDouble(params[1]);
		amount = Integer.parseInt(params[2]);
	}

	protected abstract int getLineNumber();

	public List<Double> buildParams() {

		double step = (last - start) / (amount - 1.);
		List<Double> response = new ArrayList<>();

		for (double param = start; param <= last; param += step)
			response.add(param);

		return response;
	}
}
