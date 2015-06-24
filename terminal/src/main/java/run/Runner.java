package run;

import iterators.*;
import model.*;
import settings.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

    public static String dataPath = "d:/Projects/Alfa/java/v1.0/tradeFramework/terminal/data";

    public static void main(String[] args) throws Throwable {

        AlfaGateway gateway = AlfaSettings.createGateway();

        ICandlesIterator iterator = new CacheCandlesIterator(new AlfaCandlesIterator(gateway));
        IOrdersExecutor ordersExecutor = new AlfaOrdersExecutor(gateway);

        PortfoliosInitializer initializer = new PortfoliosInitializer();

        ICandlesIterator candlesIterator = new CacheCandlesIterator(iterator);
        Trader trader = new Trader(candlesIterator, ordersExecutor, initializer);
        trader.trade();
    }
}
