package model;

import run.*;
import tools.*;

import java.io.*;

/**
 * Created by ledenev.p on 16.12.2015.
 */
public class ResearcherDataWriter {

    public static String resultPath = Researcher.settingPath + "results";

    private ResearcherDataCollector dataCollector;
    private String strategyName;

    public ResearcherDataWriter(ResearcherDataCollector dataCollector, String strategyName) throws Throwable {
        this.dataCollector = dataCollector;
        this.strategyName = strategyName;

        clean();
    }

    public void clean() throws Throwable {

        Log.info("Clean file: " + getFileName());
        PrintWriter writer = new PrintWriter(getFileName(), "utf-8");
        writer.write(dataCollector.printHeader() + "\n");
        writer.close();
    }

    public void write() throws Throwable {

        Log.info("Open file to write: " + getFileName());

        PrintWriter writer = new PrintWriter(getFileName());

        writer.write(dataCollector.print());

        writer.close();
    }

    public void append() throws Throwable {

        Log.info("Open file to append: " + getFileName());

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFileName(), true)));

        writer.write(dataCollector.printLast());

        writer.close();
    }

    private String getFileName() {
        return resultPath + "/" + strategyName + "_params_field.csv";
    }
}
