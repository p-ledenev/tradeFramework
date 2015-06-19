package iterators;

import lombok.AllArgsConstructor;
import model.Candle;
import model.ICandlesIterator;
import model.Portfolio;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesInitializer implements IPortfolioCandlesIterator {

    private ICandlesIterator iterator;

    public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        int requiredSize = portfolio.computeInitialCandlesSize();
        List<Candle> candles = new ArrayList<Candle>();

        DateTime dateTo = DateTime.now();
        DateTime dateFrom = dateTo.minusDays(3);

        int siftedSize = 0;
        while (siftedSize < requiredSize) {
            candles = iterator.getNextCandlesFor(portfolio.getSecurity(), dateFrom, dateTo);

            siftedSize = portfolio.computeStorageSizeFor(candles);
            dateFrom = dateFrom.minusDays(1);
        }

        return candles;
    }
}
