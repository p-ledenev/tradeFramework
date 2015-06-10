package run;

import alfa.AlfaCandlesIterator;
import alfa.AlfaGateway;
import alfa.AlfaOrdersExecutor;
import model.*;
import settings.PortfolioInitializer;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<Portfolio> portfolios = PortfolioInitializer.initialize();

        AlfaGateway alfaGateway = createAlfaGateway();

        ICandlesIterator alfaIterator = new AlfaCandlesIterator(alfaGateway);
        IOrdersExecutor ordersExecutor = new AlfaOrdersExecutor(alfaGateway);

        ICandlesIterator candlesIterator = new CashCandlesIterator(alfaIterator);
        Trader trader = new Trader(candlesIterator, ordersExecutor, portfolios);
        trader.trade();
    }

    public static AlfaGateway createAlfaGateway() throws Throwable {

        String login = "";
        String password = "";

        return new AlfaGateway(login, password);
    }


}
