package model;

import lombok.Data;

/**
 * Created by ledenev.p on 31.03.2015.
 */

@Data
public abstract class Order {

    protected Integer id;
    protected String account;
    protected String ticket;
    protected String market;
    protected Position position;
    protected boolean isExecuted;

    protected Machine machine;

    public abstract void applyToMachine();

    public  void executed() {
        isExecuted = true;
    }
}