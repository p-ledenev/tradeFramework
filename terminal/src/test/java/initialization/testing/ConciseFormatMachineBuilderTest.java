package initialization.testing;

import commissionStrategies.*;
import decisionStrategies.algorithmic.*;
import model.*;
import org.junit.*;
import settings.*;

import java.io.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ledenev.p on 24.06.2015.
 */
public class ConciseFormatMachineBuilderTest {

    private Machine machine;

    @Before
    public void setUp() throws Throwable {
        PortfoliosInitializer initializer = new PortfoliosInitializer() {

            @Override
            public BufferedReader getReader() throws FileNotFoundException {

                String content = "USD-9.15;AveragingStrategy;0.0145\n" +
                        "50;N;0\n" +
                        "75;N;0\n" +
                        "150;N;0\n" +
                        "100;S;0\n" +
                        "125;N;0\n" +
                        "150;N;0\n" +
                        "125;N;0\n" +
                        "\n" +
                        "USD-9.15;ApproximationStrategy;0.0145\n" +
                        "250;N;0\n" +
                        "275;N;0\n" +
                        "275;N;0";

                return new BufferedReader(new StringReader(content));
            }
        };

        machine = initializer.getPortfolios().get(0).getMachines().get(3);
    }

    @Test
    public void shouldReadDepth() throws Throwable {
        assertThat(machine.getDepth(), equalTo(100));
    }

    @Test
    public void shouldReadCurrentMoney() throws Throwable {
        assertThat(machine.getCurrentMoney(), equalTo(0.));
    }

    @Test
    public void shouldReadIsBlocked() throws Throwable {
        assertFalse(machine.isBlocked());
    }

    @Test
    public void shouldReadPositionDate() throws Throwable {
        assertNotNull(machine.getPositionDate());
    }

    @Test
    public void shouldReadPositionValue() throws Throwable {
        assertThat(machine.getPositionValue(), equalTo(0.));
    }

    @Test
    public void shouldReadPositionVolume() throws Throwable {
        assertThat(machine.getPositionVolume(), equalTo(1));
    }

    @Test
    public void shouldReadPositionDirection() throws Throwable {
        assertThat(machine.getPositionDirection(), equalTo(Direction.sell));
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
