package dataSources;

import model.*;
import org.joda.time.*;
import org.joda.time.format.*;
import tools.*;

import java.io.*;
import java.util.*;

/**
 * Created by DiKey on 16.05.2015.
 */
public class FinamDataSource implements IDataSource {

    public List<TryOutCandle> readCandlesFrom(String fileName) throws Throwable {

        List<TryOutCandle> candles = new ArrayList<TryOutCandle>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        String errorMessage = "";
        int errorCount = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            if (line == "")
                continue;

            String[] data = line.split(";");
            try {
                candles.add(createCandle(data));

            } catch (Exception e) {
                errorMessage += "Error parsing " + line + "\n";
                errorCount++;
            }
        }

        reader.close();

        setAdditionalParamsTo(candles);

        if (errorCount > 0)
            Log.info("\n" + errorMessage + "total error number: " + errorCount);

        return candles;
    }

    private TryOutCandle createCandle(String[] data) {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");
        DateTime date = DateTime.parse(data[0] + " " + data[1], formatter);

        double value = Double.parseDouble(data[5]);

        return new TryOutCandle(date, value);
    }

    private void setAdditionalParamsTo(List<TryOutCandle> candles) {

        for (int i = 0; i < candles.size(); i++) {
            TryOutCandle candle = candles.get(i);
            double nextValue = (i < candles.size() - 1) ? candles.get(i + 1).getValue() : candle.getValue();

            candle.setIndex(i);
            candle.setNextValue(nextValue);
        }
    }
}
