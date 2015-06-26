package model.testing;

import model.*;
import org.joda.time.*;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by DiKey on 11.05.2015.
 */
public class PositionTest {

    private Position position;
    double value;
    int volume;

    @Before
    public void setUp() throws Throwable {

        value = 100.5;
        volume = 10;

        position = Position.opening(Direction.sell, volume, Candle.empty(new DateTime()));
        position.setValue(value);
    }

    @Test
    public void shouldComputeProfitForClosing() {

        double newValue = 100.4;

        Position newPosition = Position.closing(Candle.empty(new DateTime()));
        newPosition.setValue(newValue);

        double profit = position.computeProfit(newPosition.getValue());

        assertThat(profit, is(equalTo(-1 * (newValue - value) * volume)));
    }

    @Test
    public void shouldComputeProfitForOpening() {

        double newValue = 100.4;

        Position newPosition = Position.opening(Direction.buy, 10, Candle.empty(new DateTime()));
        newPosition.setValue(newValue);

        double profit = position.computeProfit(newPosition.getValue());

        assertThat(profit, is(equalTo(-1 * (newValue - value) * volume)));
    }
}
