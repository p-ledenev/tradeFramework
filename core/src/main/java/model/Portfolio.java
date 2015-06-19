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

    private CandlesStorage candlesStorage;
    private List<Machine> machines;
    private String security;
    private String title;
    private int lot;

    public Portfolio(String title, String security, int lot, CandlesStorage candlesStorage) {
        this.title = title;
        this.security = security;

        this.lot = lot;
        this.candlesStorage = candlesStorage;

        machines = new ArrayList<Machine>();
    }

    public Portfolio(String title, String security, CandlesStorage candlesStorage) {
        this(title, security, 100, candlesStorage);
    }

    public void addMachine(Machine machine) {
        machines.add(machine);
    }

    public void addOrderTo(List<Order> orders, List<Candle> candles) {

        candlesStorage.add(candles);

        for (Machine machine : machines)
            machine.addOrderTo(orders);
    }

    public String printStrategy() {
        return machines.get(0).getDecisionStrategyName();
    }

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

    public int computeInitialCandlesSize() {
        int size = 0;
        for (Machine machine : machines)
            if (size < machine.getInitialStorageSize())
                size = machine.getInitialStorageSize();

        return size;
    }

    public int computeStorageSizeFor(List<Candle> candles) {
        return candlesStorage.computeStorageSizeFor(candles);
    }
}
