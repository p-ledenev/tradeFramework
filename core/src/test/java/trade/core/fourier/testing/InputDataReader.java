package trade.core.fourier.testing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.complex.Complex;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Created by ledenev.p on 22.04.2016.
 */
public class InputDataReader {

	List<Complex> result;

	public static InputDataReader create() {
		return new InputDataReader();
	}

	public InputDataReader readFrom(String fileName) throws Throwable {

		URL url = this.getClass().getClassLoader().getResource(fileName);
		List<String> data = FileUtils.readLines(new File(url.toURI()));

		result = new ArrayList<>();
		for (String row : data) {
			String[] params = row.split(";");

			Complex complex;
			if (params.length > 1)
				complex = new Complex(Double.parseDouble(params[0]), Double.parseDouble(params[1]));
			else
				complex = new Complex(Double.parseDouble(params[0]));

			result.add(complex);
		}

		return this;
	}

	/*
	** inclusive from, exclusive to
	 */
	public Complex[] getData(int from, int to) {
		Complex[] array = new Complex[to - from];
		for (int i = 0; i < array.length; i++) {
			array[i] = result.get(from + i);
		}

		return array;
	}

	public Complex[] getData() {
		return getData(0, result.size());
	}

	public int size() {
		return result.size();
	}
}
