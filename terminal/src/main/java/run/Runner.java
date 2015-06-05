package run;

import alfa.AlfaOrdersExecutor;
import model.CandlesIterator;
import model.IOrdersExecutor;
import model.Portfolio;
import settings.PortfolioInitializer;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<Portfolio> portfolios = PortfolioInitializer.initialize();

        CandlesIterator candlesIterator = new CandlesIterator();

        IOrdersExecutor ordersExecutor = new AlfaOrdersExecutor();

        Trader trader = new Trader(candlesIterator, ordersExecutor, portfolios);
        trader.trade();
    }


}
