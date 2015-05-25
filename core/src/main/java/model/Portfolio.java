package model;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 01.04.2015.
 */

@Data
public class Portfolio implements IMoneyStateSupport {

    private List<Machine> machines;
    private String security;
    private String title;
    private int lot;

    public Portfolio(String title, String security, int lot) {
        this.title = title;
        this.security = security;
        this.lot = lot;

        machines = new ArrayList<Machine>();
    }

    public Portfolio(String title, String security) {
        this(title, security, 100);
    }

    public void addMachine(Machine machine) {
        machines.add(machine);
    }

    public List<Order> processCandles(List<Candle> candles) {

        List<Order> orders = new ArrayList<Order>();
        for (Machine machine : machines)
            orders.add(machine.processCandles(candles));

        return orders;
    }

    public String printStrategy() {
        return machines.get(0).getDecisionStrategyName();
    }

    @Override
    public MoneyState getCurrentState() {
        return new MoneyState(getLatestTime(), computeCurrentMoney());
    }

    private double computeCurrentMoney() {
        double currentMoney = 0;
        for (Machine machine : machines)
            currentMoney += machine.getCurrentMoney();

        return currentMoney / machines.size();
    }

    private DateTime getLatestTime() {
        DateTime date = machines.get(0).getPositionDate();

        for (Machine machine : machines)
            if (date.isBefore(machine.getPositionDate()))
                date = machine.getPositionDate();

        return date;
    }

    public int countMachines() {
        return machines.size();
    }
}
