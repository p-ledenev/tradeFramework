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
    protected OrderDirection direction;

    protected int volume;
    protected double value;

    public abstract void applyToMachine();
}