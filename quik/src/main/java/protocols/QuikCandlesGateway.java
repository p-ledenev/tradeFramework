package protocols;

import candles.jnative.QuikCommandPipeAdapter;
import candles.model.CandleParser;
import lombok.Setter;
import model.Candle;
import org.joda.time.DateTime;
import tools.Log;

import java.util.*;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikCandlesGateway {

	@Setter
	private String classCode;

	private QuikCommandPipeAdapter adapter;

	public QuikCandlesGateway() {
		adapter = new QuikCommandPipeAdapter();
	}

	public double loadLastValueFor(String security) throws Throwable {
		return adapter.getContractPrice(classCode, security, false);
	}

	public List<Candle> loadMarketData(String security, DateTime dateFrom, DateTime dateTo) throws Throwable {

		String response = adapter.getLastCandlesOf(classCode, security, QuikCommandPipeAdapter.Interval.Minute, 10, false);
		Log.info(response);

		return CandleParser.asList(response);
	}
}
