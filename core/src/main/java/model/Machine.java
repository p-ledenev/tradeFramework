package model;

import lombok.Data;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Machine {

    protected Portfolio portfolio;
    protected MachineState state;

    public String getSecurity() {
        return portfolio.getSecurity();
    }
}
