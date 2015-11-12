package settings;

import decisionStrategies.*;
import lombok.*;
import model.*;
import siftStrategies.*;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class FullFormatPortfolioBuilder implements IPortfolioBuilder {

    private String security;
    private String title;
    private String decisionStrategyName;
    private int lot;
    private double sieveParam;
    private double commission;

    @Getter
    private Portfolio portfolio;

    private void init(String line) {

        String[] params = line.split("\\t");

        security = params[0];
        title = params[1];
        decisionStrategyName = params[2];
        lot = Integer.parseInt(params[3]);
        sieveParam = Double.parseDouble(params[4]);
        commission = Double.parseDouble(params[5]);
    }

    public void build(String line) {
        init(line);

        ISiftCandlesStrategy siftStrategy = SiftCandlesStrategyFactory.createSiftStrategy(sieveParam);
        portfolio = new Portfolio(title, security, lot, new CandlesStorage(siftStrategy));
    }

    public void addMachine(String line) throws Throwable {

        DecisionStrategy decisionStrategy = DecisionStrategy.createFor(decisionStrategyName,  portfolio.getCandlesStorage());

        FullFormatMachineBuilder builder = new FullFormatMachineBuilder(line);
        builder.build(portfolio, decisionStrategy, commission);

        portfolio.addMachine(builder.getMachine());
    }

    public String serialize() {
        String result = security + "\t" + title + "\t" + decisionStrategyName + "\t" + lot + "\t" + sieveParam + "\t" + commission + "\n";

        for (Machine machine : portfolio.getMachines()) {
            FullFormatMachineBuilder builder = new FullFormatMachineBuilder(machine);
            result += builder.serialize() + "\n";
        }

        return result;
    }

    public boolean isMachineBlocked(String line) throws Throwable {
        FullFormatMachineBuilder builder = new FullFormatMachineBuilder(line);

        return builder.isBlocked();
    }
}