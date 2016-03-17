package candles;

import model.Candle;
import org.junit.*;
import protocols.QuikCandlesGateway;
import tools.Log;

import java.util.List;

/**
 * Created by ledenev.p on 17.03.2016.
 */
public class RealRequestTest {

	private QuikCandlesGateway gateway;

	@Before
	public void setUp() throws Throwable {
		gateway = new QuikCandlesGateway();
		gateway.setClassCode("SPBFUT");
	}

	@Test
	public void loadLastValue() throws Throwable {
		double value = gateway.loadLastValueFor("Si-6.16");
		Log.info("Last value " + value);
	}

	@Test
	public void loadLastCandles() throws Throwable {

		List<Candle> candles = gateway.loadMarketData("Si-6.16", null, null);

		candles.forEach(Candle::print);
	}
}
