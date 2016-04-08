package model;

import run.*;
import tools.*;
import writers.*;

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

	public ResearcherDataWriter(String strategyName, String security, Boolean intraday, int xPoints, int yPoints) throws Throwable {

		this.strategyName = strategyName;
		this.security = security;
		this.intraday = intraday;

		dataCollector = new ResearcherDataCollector();
		writerStrategy = DataWriterStrategyFactory.create(strategyName, xPoints, yPoints);

		clean();
	}

	public void addResearchResult(ResearchResult result) {
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
