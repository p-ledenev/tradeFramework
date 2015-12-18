package run;

import dataSources.*;
import model.*;
import settings.*;

import java.util.*;

/**
 * Created by ledenev.p on 15.12.2015.
 */
public class Researcher {

    public static String settingPath = "d:/Projects/Alfa/java/tradeFramework/tryOutResearcher/data/";
    //public static String settingPath = "./";

    public static void main(String[] args) throws Throwable {

        List<InitialSettings> settingsList = Runner.readSettings(settingPath);

        for (InitialSettings settings : settingsList) {

            ResearcherDataCollector researcherDataCollector = new ResearcherDataCollector();
            ResearcherDataWriter researcherDataWriter = new ResearcherDataWriter(researcherDataCollector, settings.getStrategyName());

            for (Double sieveParam : getSieveParams()) {
                for (Integer gapsNumber : getFillGapsNumbers()) {

                    double tradeCoefficient = 0;

                    for (String year : settings.getYears()) {

                        List<TryOutCandle> candles = DataSourceFactory.createDataSource().readCandlesFrom(
                                settingPath + IDataSource.sourceFolder + "/" + year + "/" + settings.getSecurity() + "_" + settings.getTimeFrame() + ".txt");

//                        IOrdersExecutor ordersExecutor = new TryOutOrdersExecutor(Integer.parseInt(year));
//
//                        CandlesIterator candlesIterator = new CandlesIterator(candles);
//
//                        Portfolio portfolio = settings.initPortfolio(sieveParam, gapsNumber);
//                        TradeDataCollector tradeDataCollector = new NoWritingTradeDataCollector(portfolio);
//
//                        Trader trader = new Trader(candlesIterator, tradeDataCollector, ordersExecutor, portfolio);
//                        trader.trade();
//
//                        double losses = tradeDataCollector.computeMaxLossesPercent();
//                        double profit = tradeDataCollector.computeEndPeriodMoneyPercent();
//
//                        tradeCoefficient += profit / losses;

                        candles = null;

                        int s = 1;
                    }

                    tradeCoefficient /= settings.getYears().size();
                    researcherDataCollector.add(sieveParam, gapsNumber, tradeCoefficient);

                    researcherDataWriter.append();
                }
            }

            int k = 1;
        }
    }

    private static List<Double> getSieveParams() {
        List<Double> sieveParams = new ArrayList<>();

        double maxSieve = 0.2;
        int steps = 50;

        for (int i = 1; i < steps; i++)
            sieveParams.add(i * maxSieve / steps);

        return sieveParams;
    }

    private static List<Integer> getFillGapsNumbers() {

        List<Integer> gapsNumbers = new ArrayList<>();

        for (int i = 1; i < 51; i++)
            gapsNumbers.add(i);

        return gapsNumbers;
    }
}
