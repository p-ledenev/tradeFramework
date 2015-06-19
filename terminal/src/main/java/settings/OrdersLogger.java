package settings;

import model.*;
import run.*;

import java.io.*;
import java.util.*;

/**
 * Created by ledenev.p on 17.06.2015.
 */
public class OrdersLogger {

    public static void log(List<Order> orders) throws Throwable {

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(Runner.dataPath + "/operations.log", true)));

        for (Order order : orders)
            writer.println(order.toString());

        writer.close();
    }
}
