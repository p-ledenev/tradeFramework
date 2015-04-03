package run;

import factories.DataSourceFactory;
import model.*;
import settings.InitialSettings;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<InitialSettings> settingsList = readSettings();
        ITerminalExecutor executor = new ExperienceTerminalExecutor();

        List<Candle> candles = DataSourceFactory.createDataSource().readCandlesFrom("file\\path");

        for (InitialSettings settings : settingsList) {
            Portfolio portfolio = settings.initPortfolio();

            ExperienceOrderProcessor processor = new ExperienceOrderProcessor(portfolio, executor, candles);
            processor.trade();
        }
    }

    private static List<InitialSettings> readSettings() {
        // TODO
        return null;
    }
}