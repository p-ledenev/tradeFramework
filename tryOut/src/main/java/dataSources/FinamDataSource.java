package dataSources;

import model.Candle;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 16.05.2015.
 */
public class FinamDataSource implements IDataSource {

    public List<Candle> readCandlesFrom(String fileName) throws Throwable {

        List<Candle> candles = new ArrayList<Candle>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line == "")
                continue;

            String[] data = line.split(";");
            candles.add(createCandle(data));
        }

        reader.close();

        return candles;
    }

    protected Candle createCandle(String[] data) {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");
        DateTime date = DateTime.parse(data[0] + " " + data[1], formatter);

        double value = Double.parseDouble(data[5]);

        return new Candle(date, value);
    }
}
