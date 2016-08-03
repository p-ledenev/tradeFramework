package trade.terminals.quik.protocols;

import org.apache.commons.io.FileUtils;
import trade.core.model.*;
import trade.core.terminals.ITerminalGatewaysFactory;
import trade.terminals.quik.orders.model.TransactionIdIterator;

import java.io.*;
import java.util.Properties;

/**
 * Created by ledenev.p on 22.03.2016.
 */

public class QuikGatewaysFactory implements ITerminalGatewaysFactory {

	public static String initFile = "./quik.properties";

	private QuikDataGateway candlesGateway;
	private QuikTransactionsGateway transactionsGateway;

	public QuikGatewaysFactory() throws Throwable {
		Properties properties = asProperties(initFile);

		TransactionIdIterator.filePath = properties.getProperty("pathToData");

		candlesGateway = new QuikDataGateway();
		candlesGateway.setPath(properties.getProperty("pathToData"));

		transactionsGateway = new QuikTransactionsGateway();

		transactionsGateway.setAccount(properties.getProperty("account"));
		transactionsGateway.setClassCode(properties.getProperty("classCode"));
		transactionsGateway.setPathToQuik(properties.getProperty("pathToQuik"));

		int delayMillis = Integer.parseInt(properties.getProperty("orderSubmissionDelayMillis"));
		transactionsGateway.setOrderSubmissionDelayMillis(delayMillis);

		transactionsGateway.setDataGateway(candlesGateway);
	}

	private Properties asProperties(String fileName) throws Throwable {

		Properties property = new Properties();
		property.load(asInputStream(fileName));

		return property;
	}

	private InputStream asInputStream(String fileName) throws IOException {
		return FileUtils.openInputStream(new File(fileName));
	}

	@Override
	public ICandlesIterator getCandleIterator() throws Throwable {
		return new QuikCandlesIterator(candlesGateway);
	}

	@Override
	public IOrdersExecutor getOrderExecutor() throws Throwable {
		return new QuikOrdersExecutor(transactionsGateway);
	}
}
