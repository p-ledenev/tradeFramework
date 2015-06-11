package run;

import lombok.AllArgsConstructor;
import model.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import settings.PortfolioInitializer;
import tools.Format;
import tools.Log;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 02.06.2015.
 */

@AllArgsConstructor
public class Trader {

    public static LocalTime tradeTo = LocalTime.of(23, 50);

    private ICandlesIterator candlesIterator;
    private IOrdersExecutor executor;
    private List<Portfolio> portfolios;

    public void trade() throws Throwable {

        while (true) {
            process();
            suspendProcessing();
        }
    }

    private void process() throws Throwable {

        List<Order> orders = new ArrayList<Order>();
        for (Portfolio portfolio : portfolios) {
            List<Candle> candles = getNextCandlesFor(portfolio);
            orders.addAll(portfolio.processCandles(candles));
        }

        for (Order order : orders)
            order.print();

        executor.execute(orders);

        boolean needSubmitTradeData = false;
        for (Order order : orders)
            if (order.applyToMachine())
                needSubmitTradeData = true;

        if (needSubmitTradeData)
            PortfolioInitializer.write(portfolios);
    }

    private List<Candle> getNextCandlesFor(Portfolio portfolio) throws Throwable {
        // TODO where to put List<Candle> portfolio or strategy?
        return null;
    }

    private void suspendProcessing() throws Throwable {

        DateTime date = DateTime.now();

        DateTime tradeBegin = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 10, 4, 50);
        DateTime tradeEnd = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), tradeTo.getHour(), tradeTo.getMinute(), 0);

        DateTime tradePauseBegin = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 18, 45, 0);
        DateTime tradePauseEnd = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 19, 0, 0);

        Period rclock = new Period(0, 0, 0, 0);
        Period postfix = new Period(10, 4, 50, 0);
        Period prefix = new Period(23 - date.getHourOfDay(), 59 - date.getMinuteOfDay(), 59 - date.getSecondOfDay(), 0);

        boolean weekend = (date.getDayOfWeek() == DateTimeConstants.SATURDAY || date.getDayOfWeek() == DateTimeConstants.SUNDAY);

        if (date.getDayOfWeek() == DateTimeConstants.SATURDAY)
            rclock = new Period(24, 0, 0, 0);

        if (date.compareTo(tradeBegin) >= 0 && date.compareTo(tradeEnd) <= 0 && !weekend) {
            postfix = new Period(0, 0, 0, 0);

            int upSeconds = 50;
            int delta = (date.getSecondOfDay() >= upSeconds) ? 60 - date.getSecondOfDay() : -date.getSecondOfDay();
            prefix = new Period(0, 0, upSeconds + delta, 0);
        }

        if (date.compareTo(tradePauseBegin) >= 0 && date.compareTo(tradePauseEnd) <= 0) {
            prefix = new Period(0, 61 - date.getMinuteOfDay(), 0, 0);
        }

        if (date.compareTo(tradeBegin) < 0) {
            prefix = new Period(0, 0, 0, 0);
            postfix = new Period(10 - date.getHourOfDay(), 04 - date.getMinuteOfDay(), 50 - date.getSecondOfDay(), 0);
        }

        Log.info("wake up " + Format.asString(date.plus(rclock).plus(prefix).plus(postfix)));

        Thread.sleep(rclock.plus(prefix).plus(postfix).getMillis());
    }
}
