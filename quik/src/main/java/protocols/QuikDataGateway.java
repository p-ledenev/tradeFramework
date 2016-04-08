package protocols;

import candles.*;
import lombok.Setter;
import model.Candle;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import tools.*;

import java.io.File;
import java.util.*;

/**
 * Created by pledenev on 05.03.2016.
 */

public class QuikDataGateway {

	@Setter
	private String path;

	public double loadLastValueFor(String security) throws Throwable {
		try {
			List<Candle> candles = readDataFile(security);
			double value = candles.get(candles.size() - 1).getValue();

			Log.debug("Last value " + value + " for security " + security + " loaded");

			return value;

		} catch (Throwable e) {
			Log.error("", e);
			throw new Exception("Can't load last value");
		}
	}

	public List<Candle> loadMarketData(String security, DateTime dateFrom, DateTime dateTo) throws InterruptedException {
		try {
			return loadMarketDataWithThrows(security, dateFrom, dateTo);
		} catch (Throwable e) {
			Log.error(e);
			Thread.sleep(60 * 1000);
		}

		return new ArrayList<>();
	}

	private List<Candle> loadMarketDataWithThrows(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {

		List<Candle> candles = readDataFile(security);

		Log.debug("Reading data from file for period " + Format.asString(dateFrom) + " - " + Format.asString(dateTo));
		Log.debug("Last read candle " + candles.get(candles.size() - 1).print());

		int beginIndex = findIndexFor(candles, dateFrom);
		int endIndex = candles.size() - 1;

		try {
			endIndex = findIndexFor(candles, dateTo);
		} catch (CandleIndexNotFoundException e) {
			if (candles.get(endIndex).hasDateGreaterThan(dateTo))
				Log.debug("Candle index not found for date " + Format.asString(dateTo));
		}

		List<Candle> response = candles.subList(beginIndex, endIndex + 1);

		if (response.size() > 0) {
			Log.debug("First returned candle " + response.get(0).print());
			Log.debug("Last returned candle " + response.get(response.size() - 1).print());
		}

		return response;
	}

	public int loadVolumeFor(String security) throws Throwable {

		List<String> lines = FileUtils.readLines(new File(path + "positions.csv"));
		for (String line : lines) {
			String[] params = line.split(";");

			if (params[0].equals(security))
				return Integer.parseInt(params[1]);
		}

		return 0;
	}

	private int findIndexFor(List<Candle> candles, DateTime date) throws CandleIndexNotFoundException {

		for (int i = candles.size() - 1; i > 0; i--) {

			if (candles.get(i).hasDate(date))
				return i;

			if (candles.get(i).hasDateGreaterThan(date) && candles.get(i - 1).hasDateLessThan(date))
				return i - 1;
		}

		throw new CandleIndexNotFoundException(date);
	}


	private List<Candle> readDataFile(String security) throws Throwable {
		List<String> lines = FileUtils.readLines(new File(path + "candles_" + security + ".csv"));

		return CandleParser.asList(lines);
	}

}
