package trade.terminals.alfa.testing;

import org.junit.*;
import trade.core.tools.Log;
import trade.terminals.alfa.model.*;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Ignore
public class RealRequestTest {

    AlfaGateway gateway;

    @Before
    public void setUp() throws Throwable {
        gateway = new AlfaGateway("pledenev", "", "FORTS", "15567-000", AlfaTimeframe.minute);
    }

    @Test
    public void loadVersion() throws Throwable {
        Log.info(gateway.loadVersion());
    }

    @Ignore
    @Test
    public void submitOrder() throws Throwable {

        double value = gateway.loadLastValueFor("USD-9.15");

        int orderNo = gateway.submit("USD-9.15", AlfaOrderDirection.B, 1, value);
        Log.info("OrderNo " + orderNo);
    }

    @Test
    public void dropOrder() throws Throwable {

        gateway.dropOrder(151432037);
    }
}
