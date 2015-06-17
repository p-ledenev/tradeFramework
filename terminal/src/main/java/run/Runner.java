package run;

import iterators.CacheCandlesIterator;
import model.*;
import settings.AlfaSettings;
import settings.PortfolioInitializer;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

    public static String dataPath = "d:/Projects/Alfa/java/v1.0/tradeFramework/terminal";

    public static void main(String[] args) throws Throwable {

        List<Portfolio> portfolios = PortfolioInitializer.initialize();

        AlfaGateway gateway = AlfaSettings.createGateway();

        ICandlesIterator iterator = new CacheCandlesIterator(new AlfaCandlesIterator(gateway));
        IOrdersExecutor ordersExecutor = new AlfaOrdersExecutor(gateway);

        ICandlesIterator candlesIterator = new CacheCandlesIterator(iterator);
        Trader trader = new Trader(candlesIterator, ordersExecutor, portfolios);
        trader.trade();
    }
}
