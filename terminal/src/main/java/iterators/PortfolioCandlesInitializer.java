package iterators;

import lombok.AllArgsConstructor;
import model.Candle;
import model.ICandlesIterator;
import model.Portfolio;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesInitializer implements IPortfolioCandlesIterator {

    private ICandlesIterator iterator;

    public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        int size = portfolio.computeInitialCandlesSize();

        List<Candle> candles = iterator.getNextCandlesFor(portfolio.getSecurity(), DateTime.now(), 3 * size);

        portfolio.computeStorageSizeFor(candles);

        return  candles;
    }
}
