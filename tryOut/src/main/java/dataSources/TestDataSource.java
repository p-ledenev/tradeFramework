package dataSources;

import model.*;
import org.joda.time.*;

import java.util.*;

/**
 * Created by ledenev.p on 18.12.2015.
 */
public class  TestDataSource implements IDataSource {

    @Override
    public List<TryOutCandle> readCandlesFrom(String fileName) throws Throwable {

        List<TryOutCandle> candles = new ArrayList<>();

        for (int i = 0; i < 200000; i++)
            candles.add(TryOutCandle.with(i * 0.5, i * 0.9, i, DateTime.now()));

        return candles;
    }
}
