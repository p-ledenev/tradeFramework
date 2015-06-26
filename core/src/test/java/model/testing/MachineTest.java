package model.testing;

import exceptions.*;
import model.*;
import org.joda.time.*;
import org.junit.*;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
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

        Position position = Position.opening(Direction.sell, volume, Candle.empty(new DateTime()));
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

        Position newPosition = Position.opening(Direction.buy, newVolume, Candle.empty(new DateTime()));
        newPosition.setValue(newValue);
        newPosition.setDate(date);

        machine.apply(newPosition);

        assertThat(machine.getCurrentMoney(), is(equalTo((-1) * (newValue - value) * volume - commission * (volume + newVolume))));
    }

    @Test
    public void applyForSameDirection() throws Throwable {

        Position newPosition = Position.opening(Direction.sell, 1, Candle.empty(new DateTime()));

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
