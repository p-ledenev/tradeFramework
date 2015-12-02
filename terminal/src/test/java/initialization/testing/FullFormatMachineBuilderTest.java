package initialization.testing;

import commissionStrategies.*;
import decisionStrategies.algorithmic.*;
import model.*;
import org.junit.*;
import settings.*;
import tools.*;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class FullFormatMachineBuilderTest {

    private Machine machine;

    @Before
    public void setUp() throws Throwable {
        PortfoliosInitializer initializer = new PortfoliosInitializer() {

            @Override
            public BufferedReader getReader() throws FileNotFoundException {

                String content = "USD-9.15\tFORTS_USD_DEV_1\tAveragingStrategy\t1\t0.0145\t1\t0.5\n" +
                        "50\t11.23\t0\t11.06.2006 10:10:56\tN\t0\t0\n" +
                        "75\t0.0\t1\t01.05.2011 12:33:35\tN\t0\t0\n" +
                        "100\t343.2\t0\t01.01.2000 00:00:00\tN\t0\t0\n" +
                        "125\t105.3\t0\t02.03.2005 12:34:47\tB\t82.56\t1000\n" +
                        "150\t10.0\t1\t18.01.2008 10:45:12\tN\t0\t0\n" +
                        "175\t760.98\t1\t22.01.2009 22:22:12\tN\t0\t0\n" +
                        "\n" +
                        "USD-9.15\tFORTS_USD_APP_1\tApproximationStrategy\t1\t0.0145\t27\t0.5\n" +
                        "50\t50.5\t1\t14.01.2004 00:00:00\tN\t0\t0\n" +
                        "100\t78.8\t0\t02.04.2012 00:00:00\tN\t0\t0\n" +
                        "125\t91.3\t1\t01.05.2015 00:00:00\tN\t0\t0";

                return new BufferedReader(new StringReader(content));
            }

            @Override
            public IPortfolioBuilder createBuilder() {
                return new FullFormatPortfolioBuilder();
            }
        };

        machine = initializer.getPortfolios().get(0).getMachines().get(3);
    }

    @Test
    public void shouldReadDepth() throws Throwable {
        assertThat(machine.getDepth(), equalTo(125));
    }

    @Test
    public void shouldReadCurrentMoney() throws Throwable {
        assertThat(machine.getCurrentMoney(), equalTo(105.3));
    }

    @Test
    public void shouldReadIsBlocked() throws Throwable {
        assertFalse(machine.isBlocked());
    }

    @Test
    public void shouldReadPositionDate() throws Throwable {
        assertThat(machine.getPositionDate(), equalTo(Format.asDate("02.03.2005 12:34:47")));
    }

    @Test
    public void shouldReadPositionValue() throws Throwable {
        assertThat(machine.getPositionValue(), equalTo(82.56));
    }

    @Test
    public void shouldReadPositionVolume() throws Throwable {
        assertThat(machine.getPositionVolume(), equalTo(1000));
    }

    @Test
    public void shouldReadPositionDirection() throws Throwable {
        assertThat(machine.getPositionDirection(), equalTo(Direction.buy));
    }

    @Test
    public void shouldReadDecisionStrategy() throws Throwable {
        assertThat(machine.getDecisionStrategy(), is(instanceOf(AveragingDecisionStrategy.class)));
    }

    @Test
    public void shouldReadCommissionStrategy() throws Throwable {
        assertThat(((ScalpingCommissionStrategy) machine.getCommissionStrategy()).getCommission(), is(equalTo(0.5)));
    }
}
