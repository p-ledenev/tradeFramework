package trade.terminals.quik.orders.testing;

import org.junit.*;
import trade.core.model.Order;
import trade.core.tools.Log;
import trade.terminals.quik.protocols.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by ledenev.p on 15.03.2016.
 */

@Ignore
public class RealRequestTest {

    private QuikTransactionsGateway gateway;
    QuikDataGateway candlesGateway;

    @Before
    public void setUp() throws Throwable {

        gateway = new QuikTransactionsGateway();

        gateway.setClassCode("SPBFUT");
        gateway.setAccount("SPBFUT002Y6");

        gateway.setPathToQuik("c:\\Program Files\\QUIK\\Quik\\");

        candlesGateway = mock(QuikDataGateway.class);
        gateway.setDataGateway(candlesGateway);
    }

    @Test
    public void connect() throws Throwable {
        gateway.connect();
    }

    @Test
    public void registerCallbacks() throws Throwable {

        gateway.connect();
        gateway.registerCallbacks();

        Thread.sleep(30 * 1000);

        Log.info("Connection status " + gateway.getConnectionStatus());
    }

    @Test
    public void submitOrder() throws Throwable {

        when(candlesGateway.loadLastValueFor(any(String.class))).thenReturn(69000.);

        gateway.connect();
        gateway.registerCallbacks();

        Order order = new BuyOrderStub();
        gateway.submitTransactionBy(order);

        Thread.sleep(30 * 1000);

        Log.info("Are all orders executed? " + !gateway.hasUnfinishedTransactions());
    }

    @Test
    public void submitAndDropOrder() throws Throwable {

        when(candlesGateway.loadLastValueFor(any(String.class))).thenReturn(69000.);

        gateway.connect();
        gateway.registerCallbacks();

        Order order = new BuyOrderStub();
        gateway.submitTransactionBy(order);

        Thread.sleep(10 * 1000);

        gateway.dropUnfinishedTransactions();

        Thread.sleep(10 * 1000);



        Log.info("Are all orders executed? " + !gateway.hasUnfinishedTransactions());
    }
}
