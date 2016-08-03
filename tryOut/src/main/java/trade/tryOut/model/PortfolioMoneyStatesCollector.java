package trade.tryOut.model;

import trade.core.model.Portfolio;

/**
 * Created by DiKey on 11.05.2015.
 */
public class PortfolioMoneyStatesCollector extends MoneyStatesCollector<Portfolio> {

    public PortfolioMoneyStatesCollector(Portfolio portfolio) {
        super(portfolio);
    }

    @Override
    protected String getTitle() {
        return "average";
    }

    public int getLot() {
        return entity.getLot();
    }
}
