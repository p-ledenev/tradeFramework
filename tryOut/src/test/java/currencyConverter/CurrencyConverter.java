package currencyConverter;

import lombok.*;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.joda.time.DateTime;
import org.junit.*;
import tools.*;

import java.io.File;
import java.util.*;

/**
 * Created by ledenev.p on 28.03.2016.
 */
public class CurrencyConverter {

	String path = "d:\\Projects\\Alfa\\java\\tradeFramework\\tryOutResearcher\\data\\sources\\";

	Map<DateTime, Double> currencies;


	@Data
	@AllArgsConstructor
	private class SecurityInfo {
		private String name;
		private Double lot;
	}

	@Before
	public void setUp() throws Throwable {
		Document currencyDocument = DocumentHelper.parseText(
				FileUtils.readFileToString(new File(path + "usd_exchange.xml")));

		currencies = new HashMap<>();

		Log.info("Start currencies parsing");
		List<Node> nodes = currencyDocument.selectNodes("//rtsdata/rates/rate");
		for (Node node : nodes)
			currencies.put(
					Format.asDate(node.valueOf("@moment"), "yyyy-MM-dd HH:mm:ss"),
					Double.parseDouble(node.valueOf("@value"))
			);
		Log.info("End currencies parsing");
	}

	@Test
	public void convert() throws Throwable {

		List<SecurityInfo> infos = new ArrayList<>();
		infos.add(new SecurityInfo("rts", 0.02));
		infos.add(new SecurityInfo("ed", 1000.));
		infos.add(new SecurityInfo("brent", 10.));

		String[] years = {"2015"};

		for (SecurityInfo info : infos) {
			for (String year : years) {

				List<String> result = new ArrayList<>();

				List<String> list = FileUtils.readLines(
						new File(path + year + "\\" + info.getName() + "_1min_pp.txt"));

				int j = 0;
				Log.info("Process " + info.getName() + "; " + year);
				for (String line : list) {
					String[] params = line.split(";");

					DateTime date = Format.asDate(params[0] + " " + params[1], "dd/MM/yy HH:mm:ss");
					Double currency = getCurrency(date);

					double[] values = new double[4];
					for (int i = 0; i < 4; i++)
						values[i] = Double.parseDouble(params[i + 2]);

					for (int i = 0; i < 4; i++)
						values[i] *= Round.toAmount(currency * info.getLot(), 0);

					result.add(params[0] + ";" + params[1] + ";" + values[0] + ";" + values[1] + ";"
							+ values[2] + ";" + values[3] + ";" + params[6]);

					if ((j++) % 1000 == 0)
						Log.info(info.getName() + " " + params[0]);
				}

				FileUtils.writeLines(new File(path + year + "\\" + info.getName() + "_1min.txt"), result);
				Log.info("Processed " + info.getName() + "; " + year);
			}
		}
	}

	public double getCurrency(DateTime date) throws Throwable {

		DateTime clearingDate = date.withHourOfDay(18).withMinuteOfHour(30);

		if (date.getHourOfDay() > 18)
			clearingDate = date.withHourOfDay(18).withMinuteOfHour(30).plusDays(1);

		while (true) {

			Double currency = currencies.get(clearingDate);

			if (currency != null) {
//				Log.debug("For date " + Format.asString(date) + " found currency " + currency
//						+ " on " + Format.asString(clearingDate));

				return currency;
			}

			clearingDate = clearingDate.plusDays(1);
		}
	}

}
