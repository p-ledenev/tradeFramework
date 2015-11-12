package tools;

import org.apache.log4j.*;

import javax.annotation.concurrent.*;
import java.lang.management.*;

@NotThreadSafe
public class Log {

    private static final Logger logger = Logger.getLogger(Log.class);

    public static void hello(){
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

    public static void printJavaRuntimeInfo() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        info(bean.getInputArguments().toString());

    }

    public static void enableDebug() {
        getLogger().setLevel(Level.DEBUG);
    }

    public static void disableDebug() {
        getLogger().setLevel(Level.INFO);
    }
}
