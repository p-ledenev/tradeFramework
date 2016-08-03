package trade.core.model.testing;

import org.joda.time.DateTime;
import org.junit.*;
import trade.core.exceptions.PositionAlreadySetFailure;
import trade.core.model.*;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by DiKey on 11.05.2015.
 */
public class MachineTest {

    private Machine machine;
    private int volume;
    private double value;
    private double commission;
    DateTime date;

    @Before
    public void setUp() throws Throwable {

        value = 100.5;
        volume = 10;
        commission = 1.5;
        date = DateTime.now();

        Position position = Position.opening(Direction.Sell, volume, Candle.empty(new DateTime()));
        position.setValue(value);
        position.setDate(date);

        machine = new Machine();
        machine.setPosition(position);
        machine.setCommissionStrategy(new CommissionStrategyStub(commission));
    }

    @Test
    public void applyForClosingPosition() throws Throwable {

        double newValue = 100.3;

        Position newPosition = Position.closing(Candle.empty(new DateTime()));
        newPosition.setValue(newValue);
        newPosition.setDate(date);

        machine.apply(newPosition);

        assertThat(machine.getCurrentMoney(), is(equalTo(((-1) * (newValue - value) - commission) * volume)));
    }

    @Test
    public void applyForOpeningPosition() throws Throwable {

        double newValue = 100.3;
        int newVolume = 20;

        Position newPosition = Position.opening(Direction.Buy, newVolume, Candle.empty(new DateTime()));
        newPosition.setValue(newValue);
        newPosition.setDate(date);

        machine.apply(newPosition);

        assertThat(machine.getCurrentMoney(), is(equalTo((-1) * (newValue - value) * volume - commission * (volume + newVolume))));
    }

    @Test
    public void applyForSameDirection() throws Throwable {

        Position newPosition = Position.opening(Direction.Sell, 1, Candle.empty(new DateTime()));

        Portfolio portfolio = mock(Portfolio.class);
        machine.setPortfolio(portfolio);

        when(portfolio.printStrategy()).thenReturn("print portfolio strategy called");

        try {
            machine.apply(newPosition);
            assertTrue(false);
        } catch (PositionAlreadySetFailure e) {
        }
    }
}
