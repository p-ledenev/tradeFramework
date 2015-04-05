package model;

import lombok.Data;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Machine {

    protected Portfolio portfolio;
    protected Slice state;

    protected int depth;
    protected double currentMoney;
    protected Position position;

    public String getSecurity() {
        return portfolio.getSecurity();
    }

    public void add(Position position) {


        currentMoney += position.computeAmount();
        this.position = position;
        position.date = null;
    }
}
