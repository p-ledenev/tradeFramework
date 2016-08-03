package trade.researcher.tryOut.model;

import trade.researcher.tryOut.run.Researcher;
import trade.core.tools.*;
import trade.researcher.tryOut.writers.*;

import java.io.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */
public class ResearcherDataWriter {

	public static String resultPath = Researcher.settingPath + "results";

	private DataWriterStrategy writerStrategy;
	private ResearcherDataCollector dataCollector;
	private String strategyName;
	private String security;
	private Boolean intraday;

	public ResearcherDataWriter(
			DataWriterStrategy writerStrategy,
			String strategyName,
			String security,
			Boolean intraday) throws Throwable {

		this.strategyName = strategyName;
		this.security = security;
		this.intraday = intraday;
		this.writerStrategy = writerStrategy;

		dataCollector = new ResearcherDataCollector();
		clean();
	}

	public void addResearchResult(IResearchResult result) {
		dataCollector.add(result);
	}

	public void clean() throws Throwable {

		Log.info("Clean file: " + getFileName());
		PrintWriter writer = new PrintWriter(getFileName(), "utf-8");
		writer.write(writerStrategy.printHeader() + "\n");
		writer.close();
	}

	public void write() throws Throwable {

		Log.info("Open file to write: " + getFileName());

		PrintWriter writer = new PrintWriter(getFileName());

		writer.write(dataCollector.print(writerStrategy));

		writer.close();
	}

	public void appendToFile() throws Throwable {

		Log.info("Open file to append: " + getFileName());

		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFileName(), true)));

		writer.write(writerStrategy.print(dataCollector.getLast()) + "\n");

		writer.close();
	}

	private String getFileName() {
		return resultPath + "/" + strategyName + "_" + security + (intraday ? "_intraday" : "") +
				"_field_params." + writerStrategy.getFileExtension();
	}
}
