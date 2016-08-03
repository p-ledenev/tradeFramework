package trade.tryOut.resultWriters;

import lombok.*;
import trade.core.model.Portfolio;
import trade.core.tools.Log;
import trade.tryOut.settings.InitialSettings;

import java.io.PrintWriter;

/**
 * Created by DiKey on 11.05.2015.
 */

@Getter @Setter
@AllArgsConstructor
public abstract class ResultWriter {

    public static String resultPath = InitialSettings.settingPath + "results";

    private String fileName;

    public void write() throws Throwable {

        PrintWriter writer = createPrintWriterFor();

        writeTo(writer);
        Log.info("writing success");

        writer.close();
        Log.info("file closed");
    }

    protected abstract void writeTo(PrintWriter writer);

    private PrintWriter createPrintWriterFor() throws Throwable {

        String security = getPortfolio().getSecurity();
        String strategy = getPortfolio().getDecisonStrategyName();
        Double sieveParam = getPortfolio().getSieveParam();
        Integer fillingGapsNumber = getPortfolio().getFillingGapsNumber();
        int year = getYear();

        String filePath = resultPath + "/" + fileName + "_" + security + "_" + year + "_" +
                sieveParam + "_" + fillingGapsNumber + "_" + strategy + ".csv";

        Log.info("Open file to write: " + filePath);

        return new PrintWriter(filePath, "utf-8");
    }

    protected abstract Portfolio getPortfolio();

    protected abstract int getYear();
}
