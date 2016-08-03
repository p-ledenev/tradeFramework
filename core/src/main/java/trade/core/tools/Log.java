package trade.core.tools;

import org.apache.log4j.Logger;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class Log {

	private static final Logger logger = Logger.getLogger(Log.class);

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void error(String message, Throwable exc) {
		logger.error(message, exc);
	}

	public static void error(Throwable exc) {
		logger.error("", exc);
	}

	public static void error(String message) {
		logger.error(message);
	}
}
