package model;

import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */
public interface IOrdersExecutor {

    public void execute(List<Order> orders) throws Throwable;
}
