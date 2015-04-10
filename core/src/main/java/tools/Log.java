package tools;

import org.apache.log4j.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Properties;

public class Log {

	private static Logger logger;

	public static void configureLogger(String configName) {
		PropertyConfigurator.configure(configName);
		logger = Logger.getLogger(Log.class);

		logger.info("Hello!!!");
		printJavaRuntimeInfo();
	}

	public static void configureLogger(Properties loggerCfg) {
		PropertyConfigurator.configure(loggerCfg);
		logger = Logger.getLogger(Log.class);

		logger.info("Hello!!!");
		printJavaRuntimeInfo();
	}

	public static void setLogger(Logger logger) {
		Log.logger = logger;
	}

	public static Logger getLogger() {
		if (logger == null)
			configureLogger("log4j.config");
		return logger;
	}


	public static void trace(String message) {
		getLogger().trace(message);
	}

	public static void debug(String message) {
		getLogger().debug(message);
	}

	public static void info(String message) {
		getLogger().info(message);
	}

	public static void error(String message, Throwable exc) {
		getLogger().error(message, exc);
	}

	public static void error(String message) {
		getLogger().error(message);
	}

	public static void fatal(String message) {
		getLogger().fatal(message);
	}

	public static void fatal(String message, Throwable exc) {
		getLogger().fatal(message, exc);
	}

	public static boolean isDebugEnabled() {
		return getLogger().isDebugEnabled();
	}

	public static boolean isTraceEnabled() {
		return getLogger().isTraceEnabled();
	}

	public static void printJavaRuntimeInfo() {
		RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
		info(bean.getInputArguments().toString());

	}
}
