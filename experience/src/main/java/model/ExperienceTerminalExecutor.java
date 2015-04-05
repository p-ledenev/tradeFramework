package model;

import java.util.List;

/**
 * Created by ledenev.p on 02.04.2015.
 */
public class ExperienceTerminalExecutor implements ITerminalExecutor {

    public void execute(List<Order> orders) {
        for(Order order : orders)
            order.executed();
    }
}
