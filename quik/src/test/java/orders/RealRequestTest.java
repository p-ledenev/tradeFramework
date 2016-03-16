package orders;

import model.Order;
import org.junit.*;
import protocols.*;
import tools.Log;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by ledenev.p on 15.03.2016.
 */
public class RealRequestTest {

	private QuikTransactionsGateway gateway;
	QuikCandlesGateway candlesGateway;

	@Before
	public void setUp() throws Throwable {

		gateway = new QuikTransactionsGateway();
		gateway.setClassCode("SPBFUT");
		gateway.setPathToQuik("c:\\Program Files\\QUIK\\Quik\\");

		candlesGateway = mock(QuikCandlesGateway.class);
		gateway.setCandlesGateway(candlesGateway);
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

		when(candlesGateway.loadLastValueFor(any(String.class))).thenReturn(74000.);

		gateway.connect();
		gateway.registerCallbacks();

		Order order = new BuyOrderStub();

		gateway.submitTransactionBy(order);

		Thread.sleep(30 * 1000);
		Log.info("Are all orders executed? " + !gateway.hasUnfinishedTransactions());
	}
}
