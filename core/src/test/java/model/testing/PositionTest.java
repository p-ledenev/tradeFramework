package model.testing;

import model.OrderDirection;
import model.Position;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

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

        position = Position.opening(OrderDirection.sell, volume, new DateTime());
        position.setValue(value);
    }

    @Test
    public void shouldComputeProfitForClosing() {

        double newValue = 100.4;

        Position newPosition = Position.closing(new DateTime());
        newPosition.setValue(newValue);

        double profit = position.computeProfit(newPosition.getValue());

        assertThat(profit, is(equalTo(-1 * (newValue - value) * volume)));
    }

    @Test
    public void shouldComputeProfitForOpening() {

        double newValue = 100.4;

        Position newPosition = Position.opening(OrderDirection.buy, 10, new DateTime());
        newPosition.setValue(newValue);

        double profit = position.computeProfit(newPosition.getValue());

        assertThat(profit, is(equalTo(-1 * (newValue - value) * volume)));
    }
}
