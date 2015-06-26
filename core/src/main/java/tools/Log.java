package tools;

import org.slf4j.*;

import java.lang.management.*;

public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    public static void configureLogger() {
        logger.info("Hello!!!");
        printJavaRuntimeInfo();
    }

    public static Logger getLogger() {
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
