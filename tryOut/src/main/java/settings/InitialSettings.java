package settings;

import commissionStrategies.*;
import decisionStrategies.*;
import lombok.*;
import model.*;
import siftStrategies.*;
import takeProfitStrategies.*;

import java.util.*;

/**
 * Created by ledenev.p on 02.04.2015.
 */

@Data
@NoArgsConstructor
public class InitialSettings {

    //public static String settingPath = "F:/Teddy/Alfa/java/v1.0/tradeFramework/tryOut/data/";
    public static String settingPath = "d:/Projects/Alfa/java/v1.0/tradeFramework/tryOut/data/";
    //public static String settingPath = "./";

    private String security;
    private String timeFrame;
    private double sieveParam;
    private List<Integer> depths;
    private List<String> years;
    private String strategyName;
    private double commission;

    public static InitialSettings createFrom(String line) {

        String[] data = line.split("~&~");

        InitialSettings settings = new InitialSettings();

        settings.setStrategyName(data[2]);
        settings.setSecurity(data[0]);
        settings.setTimeFrame(data[1]);
        settings.setSieveParam(Double.parseDouble(data[4]));
        settings.setCommission(Double.parseDouble(data[3]));

        settings.setYears(Arrays.asList(data[5].split(";")));

        List<Integer> depths = new ArrayList<Integer>();
        String[] strDepths = data[6].split(";");
        for (String depth : strDepths)
            depths.add(Integer.parseInt(depth));

        settings.setDepths(depths);

        return settings;
    }

    public Portfolio initPortfolio(List<TryOutCandle> allData) throws Throwable {

        ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam);
        CandlesStorage candlesStorage = new CandlesStorage(siftStrategy);
        Portfolio portfolio = new Portfolio(strategyName, security, candlesStorage);

        for (int depth : depths) {
            ITakeProfitStrategy profitStrategy = TakeProfitStrategyFactory.createTakeProfitStrategy();

            DecisionStrategy decisionStrategy = DecisionStrategy.createFor(strategyName, profitStrategy, candlesStorage);
            ICommissionStrategy commissionStrategy = new ScalpingCommissionStrategy(commission);

            setSpecificParamsFor(decisionStrategy, allData, siftStrategy);

            Machine machine = new Machine(portfolio, decisionStrategy, commissionStrategy, depth);
            portfolio.addMachine(machine);
        }

        return portfolio;
    }

    private void setSpecificParamsFor(DecisionStrategy strategy, List<TryOutCandle> allData, ISiftCandlesStrategy siftStrategy) {

        if (strategy.getClass() == NeuroTrainingDecisionStrategy.class) {
            Candle[] candlesArray = allData.toArray(new Candle[allData.size()]);
            CandlesStorage allDataStorage = new CandlesStorage(siftStrategy, Arrays.asList(candlesArray));
            ((NeuroTrainingDecisionStrategy) strategy).setAllDataStorage(allDataStorage);
        }
    }
}