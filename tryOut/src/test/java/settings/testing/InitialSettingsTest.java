package settings.testing;

import model.*;
import org.junit.*;
import settings.*;
import siftStrategies.*;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class InitialSettingsTest {

    Portfolio portfolio;

    @Before
    public void setUp() throws Throwable {

        String line = "usd~&~1min~&~AveragingStrategy~&~0.25~&~0.0342~&~27~&~2014;2015~&~50;75";
        InitialSettings settings = InitialSettings.createFrom(line);

        portfolio = settings.initPortfolio(new ArrayList<TryOutCandle>());
    }

    @Test
    public void shouldReadAllMachines() throws Throwable {
        assertThat(portfolio.getMachines().size(), equalTo(2));

        assertThat(portfolio.getMachines().get(0).getDepth(), equalTo(50));
        assertThat(portfolio.getMachines().get(1).getDepth(), equalTo(75));
    }

    @Test
    public void shouldReadTitle() throws Throwable {
        assertThat(portfolio.getTitle(), equalTo("AveragingStrategy"));
    }

    @Test
    public void shouldReadStrategyName() throws Throwable {
        assertThat(portfolio.getDecisonStrategyName(), equalTo("AveragingStrategy"));
    }

    @Test
    public void shouldReadSecurity() throws Throwable {
        assertThat(portfolio.getSecurity(), equalTo("usd"));
    }

    @Test
    public void shouldReadSieveParam() throws Throwable {
        assertThat(((MinMaxSiftStrategy) portfolio.getCandlesStorage().getSiftStrategy()).getSieveParam(), equalTo(0.0342));
    }

    @Test
    public void shouldReadFillingGapsNumber() throws Throwable {
        assertThat(((MinMaxSiftStrategy) portfolio.getCandlesStorage().getSiftStrategy()).getFillingGapsNumber(), equalTo(27));
    }

    @Test
    public void shouldSetPortfolioToMachine() throws Throwable {
        assertThat(portfolio, equalTo(portfolio.getMachines().get(1).getPortfolio()));
    }
}
