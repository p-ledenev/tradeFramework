package model;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by ledenev.p on 02.04.2015.
 */

@Data
public class MachineState {

    protected DateTime date;
    protected double money;

    public MachineState(DateTime date, double money) {
        this.date = date;
        this.money = money;
    }

    public String printCSV() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        return formatter.print(date) + "; " + money;
    }

    public boolean equals(MachineState state) {

        if (state == null)
            return false;

        return state.hasEqualMoney(money);
    }

    public boolean hasEqualMoney(double money) {
        return this.money == money;
    }
}
