package initialization.testing;

import model.*;
import org.junit.*;
import settings.*;
import siftStrategies.*;

import java.io.*;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class PortfolioBuilderTest {

    List<Portfolio> portfolios;

    @Before
    public void setUp() throws Throwable {
        PortfoliosInitializer initializer = new PortfoliosInitializer() {

            @Override
            public BufferedReader getReader() throws FileNotFoundException {

                String content = "USD-9.15\tFORTS_USD_DEV_1\tAveragingStrategy\t1\t0.0145\t0.5\n" +
                        "50\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "75\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "100\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "125\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "150\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "175\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "\n" +
                        "USD-9.15\tFORTS_USD_APP_1\tApproximationStrategy\t1\t0.0145\t0.5\n" +
                        "50\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "100\t0\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "125\t0\t0\t01.01.2000 00:00:00\tN\t0\t0";

                return new BufferedReader(new StringReader(content));
            }
        };

        portfolios = initializer.getPortfolios();
    }

    @Test
    public void shouldReadAllPortfolios() throws Throwable {
        assertThat(portfolios.size(), equalTo(2));
    }

    @Test
    public void shouldReadAllMachines() throws Throwable {
        assertThat(portfolios.get(0).getMachines().size(), equalTo(6));
        assertThat(portfolios.get(1).getMachines().size(), equalTo(3));
    }

    @Test
    public void shouldReadTitle() throws Throwable {
        assertThat(portfolios.get(0).getTitle(), equalTo("FORTS_USD_DEV_1"));
    }

    @Test
    public void shouldReadStrategyName() throws Throwable {
        assertThat(portfolios.get(0).getDecisonStrategyName(), equalTo("AveragingStrategy"));
    }

    @Test
    public void shouldReadSecurity() throws Throwable {
        assertThat(portfolios.get(0).getSecurity(), equalTo("USD-9.15"));
    }

    @Test
    public void shouldReadLot() throws Throwable {
        assertThat(portfolios.get(0).getLot(), equalTo(1));
    }

    @Test
    public void shouldReadSeiveParam() throws Throwable {
        assertThat(((MinMaxSiftStrategy) portfolios.get(0).getCandlesStorage().getSiftStrategy()).getSieveParam(), equalTo(0.0145));
    }

    @Test
    public void shouldSetPortolioToMachine() throws Throwable {
        assertThat(portfolios.get(0), equalTo(portfolios.get(0).getMachines().get(3).getPortfolio()));
    }
}
