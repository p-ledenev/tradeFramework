package settings;

import commissionStrategies.*;
import decisionStrategies.*;
import lombok.*;
import model.*;
import siftStrategies.*;
import takeProfitStrategies.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class ConciseFormatPortfolioBuilder implements IPortfolioBuilder {

    private String security;
    private String decisionStrategyName;
    private double sieveParam;

    @Getter
    private Portfolio portfolio;

    private void init(String line) {
        String[] params = line.split(";");

        security = params[0];
        decisionStrategyName = params[1];
        sieveParam = Double.parseDouble(params[2]);
    }

    public void build(String line) {
        init(line);

        ISiftCandlesStrategy siftStrategy = new MinMaxSiftStrategy(sieveParam);
        portfolio = new Portfolio(decisionStrategyName + "_" + security, security, 1, new CandlesStorage(siftStrategy));
    }

    public void addMachine(String line) throws Throwable {

        ITakeProfitStrategy profitStrategy = new NoTakeProfitStrategy();
        ICommissionStrategy commissionStrategy = new ScalpingCommissionStrategy(0.5);
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor(decisionStrategyName, profitStrategy, portfolio.getCandlesStorage());

        ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(line);
        builder.build();
        builder.init(portfolio, decisionStrategy, commissionStrategy);

        portfolio.addMachine(builder.getMachine());
    }

    public String serialize() {
        String result = security + ";" + decisionStrategyName + ";" + sieveParam + "\n";

        for (Machine machine : portfolio.getMachines()) {
            ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(machine);
            result += builder.serialize() + "\n";
        }

        return result;
    }

    public boolean isMachineBlocked(String line) throws Throwable {
        ConciseFormatMachineBuilder builder = new ConciseFormatMachineBuilder(line);

        return builder.isBlocked();
    }
}
