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
public class PortfolioBuilder {

    private String security;
    private String title;
    private String decisionStrategyName;
    private int lot;
    private double sieveParam;
    private double commission;

    @Getter
    private Portfolio portfolio;

    public PortfolioBuilder(String line) {
        String[] params = line.split("\\t");

        security = params[0];
        title = params[1];
        decisionStrategyName = params[2];
        lot = Integer.parseInt(params[3]);
        sieveParam = Double.parseDouble(params[4]);
        commission = Double.parseDouble(params[5]);
    }

    public PortfolioBuilder(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void create() {
        ISiftCandlesStrategy siftStrategy = new MinMaxSiftStrategy(sieveParam);
        portfolio = new Portfolio(title, security, lot, new CandlesStorage(siftStrategy));
    }

    public void init(String line) throws Throwable {

        ITakeProfitStrategy profitStrategy = new NoTakeProfitStrategy();
        ICommissionStrategy commissionStrategy = new ScalpingCommissionStrategy(commission);
        DecisionStrategy decisionStrategy = DecisionStrategy.createFor(decisionStrategyName, profitStrategy, portfolio.getCandlesStorage());

        MachineBuilder builder = new MachineBuilder(line);
        builder.create();
        builder.init(portfolio, decisionStrategy, commissionStrategy);
    }

    public String serialize() {
        String result = portfolio.getSecurity() + "\t" + portfolio.getSecurity() + "\t" + portfolio.getDecisonStrategyName() + "\n";

        for (Machine machine : portfolio.getMachines()) {
            MachineBuilder builder = new MachineBuilder(machine);
            result += builder.serialize() + "\n";
        }

        return result;
    }
}
