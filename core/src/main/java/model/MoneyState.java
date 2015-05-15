package model;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import tools.Format;

/**
 * Created by ledenev.p on 02.04.2015.
 */

@Data
public class MoneyState {

    protected DateTime date;
    protected double money;

    public MoneyState(DateTime date, double money) {
        this.date = date;
        this.money = money;
    }

    public String printCSV() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

        return Format.indexFor(date) + ";" + formatter.print(date) + ";" + money;
    }

    public boolean equals(MoneyState state) {

        if (state == null)
            return false;

        return state.hasEqualMoney(money);
    }

    public boolean hasEqualMoney(double money) {
        return this.money == money;
    }
}
