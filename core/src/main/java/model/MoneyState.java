package model;

import lombok.*;
import org.joda.time.*;
import tools.*;

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
        return Format.indexFor(date) + ";" + Format.asString(date) + ";" + Round.toThree(money);
    }

    public String printCSVPercent(double amount) {
        return Format.indexFor(date) + ";" + Format.asString(date) + ";" + getMoneyPercent(amount);
    }

    public boolean equals(MoneyState state) {
        return state.hasEqualMoney(money);
    }

    public double getMoneyPercent(double amount) {
        return Round.toMoneyAmount(money / amount * 100);
    }

    public boolean hasEqualMoney(double money) {
        return this.money == money;
    }
}
