package dataSources;

import model.Candle;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public abstract class DataSource {

    public abstract List<Candle> readCandlesFrom(String fileName);
}
