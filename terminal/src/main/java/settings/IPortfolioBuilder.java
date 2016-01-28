package settings;

import model.*;

/**
 * Created by ledenev.p on 09.09.2015.
 */
public interface IPortfolioBuilder {

    void build(String line);

    void addMachine(String line) throws Throwable;

    String serialize();

    boolean isMachineBlocked(String line) throws Throwable;

    Portfolio getPortfolio();
}
