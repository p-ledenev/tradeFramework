package resultWriters;

import lombok.*;
import model.Portfolio;

import java.io.PrintWriter;

/**
 * Created by DiKey on 11.05.2015.
 */

@Data
@AllArgsConstructor
public abstract class ResultWriter {

    public static String resultPath = "results";

    private String fileName;

    public void write() throws Throwable {

        PrintWriter writer = createPrintWriterFor(fileName);

        writeTo(writer);

        writer.close();
    }

    protected abstract void writeTo(PrintWriter writer);

    private PrintWriter createPrintWriterFor(String fileName) throws Throwable {

        String security = getPortfolio().getSecurity();
        String title = getPortfolio().getTitle();
        int year = getYear();

        return new PrintWriter(resultPath + "\\" + fileName + "_" + security + "_" + year + "_" + title + ".csv", "utf-8");
    }

    protected abstract Portfolio getPortfolio();

    protected abstract int getYear();
}
