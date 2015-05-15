package run;

import factories.DataSourceFactory;
import model.*;
import resultWriters.ResultWriter;
import runner.ICandlesIterator;
import settings.InitialSettings;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<InitialSettings> settingsList = readSettings();

        List<Candle> candles = DataSourceFactory.createDataSource().readCandlesFrom("file\\path");
        IOrdersExecutor executor = new ExperienceOrdersExecutor(candles);

        ICandlesIterator candlesIterator = new CandlesIterator(candles);

        runner.Trader trader = new runner.Trader();
        trader.trade();
    }

    private static List<InitialSettings> readSettings() {
        // TODO
        return null;
    }

    private static void ttt() {
        for (InitialSettings settings : settingsList) {
            Portfolio portfolio = settings.initPortfolio();

            Trader trader;
            if (portfolio.countMachines() == 1)
                trader = new SingleMachineTrader(portfolio, executor, candles);
            else
                trader = new CommonTrader(portfolio, executor, candles);

            trader.trade();

            List<ResultWriter> writers = trader.getResultsWriters();
            for (ResultWriter writer : writers)
                writer.write();
        }
    }
}
