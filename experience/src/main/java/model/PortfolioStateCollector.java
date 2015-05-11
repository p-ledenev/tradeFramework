package model;

/**
 * Created by DiKey on 11.05.2015.
 */
public class PortfolioStateCollector extends StatesCollector<Portfolio> {

    public PortfolioStateCollector(Portfolio portfolio) {
        super(portfolio);
    }

    @Override
    protected String getTitle() {
        return "average";
    }
}
