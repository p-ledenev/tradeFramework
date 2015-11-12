package model;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by DiKey on 11.08.2015.
 */
public class ResultWriter {

    private String fileName;

    public ResultWriter(String fileName) {
        this.fileName = fileName;
    }

    public void write(List<TrainingResult> results) throws Throwable {

        PrintWriter writer = new PrintWriter(fileName);
        for (TrainingResult result : results)
            writer.write(result.print() + "\n");

        writer.close();
    }
}