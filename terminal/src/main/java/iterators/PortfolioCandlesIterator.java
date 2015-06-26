package iterators;

import lombok.*;
import model.*;
import org.joda.time.*;

import java.util.*;

/**
 * Created by ledenev.p on 17.06.2015.
 */

@AllArgsConstructor
public class PortfolioCandlesIterator implements IPortfolioCandlesIterator {

    private ICandlesIterator iterator;

    public List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        DateTime dateFrom = portfolio.getLastCandle().getDate();

        DateTime now = DateTime.now();
        DateTime dateTo = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), now.getHourOfDay(), now.getMinuteOfHour());

        return iterator.getNextCandlesFor(portfolio.getSecurity(), dateFrom, dateTo);
    }
}
