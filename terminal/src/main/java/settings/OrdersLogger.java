package settings;

import model.Order;
import run.Runner;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by ledenev.p on 17.06.2015.
 */
public class OrdersLogger {

    public static void log(List<Order> orders) throws Throwable {

        PrintWriter writer = new PrintWriter(Runner.dataPath + "/operations.log");

        for (Order order : orders)
            writer.println(order.toString());
    }
}
