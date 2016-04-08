package settings;

import model.*;
import org.apache.commons.io.FileUtils;
import terminals.ITerminalGatewaysFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by ledenev.p on 22.03.2016.
 */

public class AlfaGatewaysFactory implements ITerminalGatewaysFactory {

	private AlfaGateway gateway;

	public AlfaGatewaysFactory() throws Throwable {
		gateway = AlfaSettings.createGateway();
		gateway.readPassword();
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
		return new AlfaCandlesIterator(gateway);
	}

	@Override
	public IOrdersExecutor getOrderExecutor() throws Throwable {
		return new AlfaOrdersExecutor(gateway);
	}
}
