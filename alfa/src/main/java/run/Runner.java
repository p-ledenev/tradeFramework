package run;

import model.Portfolio;
import settings.PortfolioInitializer;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public class Runner {

    public static void main(String[] args) throws Throwable {

        List<Portfolio> portfolios = PortfolioInitializer.initialize();


    }


}
