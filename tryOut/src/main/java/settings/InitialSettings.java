package settings;

import commissionStrategies.ICommissionStrategy;
import commissionStrategies.ScalpingCommissionStrategy;
import decisionStrategies.DecisionStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Machine;
import model.Portfolio;
import model.Position;
import siftStrategies.ISiftCandlesStrategy;
import siftStrategies.SiftCandlesStrategyFactory;
import takeProfitStrategies.ITakeProfitStrategy;
import takeProfitStrategies.TakeProfitStrategyFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
@Data
@NoArgsConstructor
public class InitialSettings {

    public static String settingPath = "F:/Teddy/Alfa/java/v1.0/tradeFramework/tryOut/data";

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

    public Portfolio initPortfolio() throws Throwable {
        Portfolio portfolio = new Portfolio(strategyName, security);

        for (int depth : depths) {
            ITakeProfitStrategy profitStrategy = TakeProfitStrategyFactory.createTakeProfitStrategy();
            ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam);

            DecisionStrategy decisionStrategy = DecisionStrategy.createFor(strategyName, profitStrategy, siftStrategy);
            ICommissionStrategy commissionStrategy = new ScalpingCommissionStrategy(commission);

            Machine machine = new Machine(portfolio, depth, 1000000, Position.closing(), decisionStrategy, commissionStrategy);
            portfolio.addMachine(machine);
        }

        return portfolio;
    }
}