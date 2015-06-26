package model;

import lombok.*;
import org.joda.time.*;
import tools.*;

import java.io.*;
import java.util.*;

/**
 * Created by DiKey on 11.05.2015.
 */
@Getter
public abstract class MoneyStatesCollector<TEntity extends IMoneyStateSupport> {

    protected TEntity entity;
    protected List<MoneyState> states;
    @Setter
    protected double initialMoneyAmount;

    public MoneyStatesCollector(TEntity entity) {
        this.entity = entity;

        states = new ArrayList<MoneyState>();
    }

    public boolean isEmpty() {
        return states.size() <= 0;
    }

    public void addStateIfChanged() {

        if (entity.getCurrentState().equals(getLastState()))
            return;

        states.add(entity.getCurrentState());
    }

    protected MoneyState getLastState() {
        if (states.size() == 0)
            return new MoneyState(new DateTime(0), 0);

        return states.get(states.size() - 1);
    }

    public int getYear() {
        return getLastState().getDate().getYear();
    }

    public String printState(int index) {
        if (states.size() <= index)
            return ";;";

        return states.get(index).printCSVPercent(initialMoneyAmount);
    }

    public void writeStatesTo(PrintWriter writer) {
        for (MoneyState state : states)
            writer.write(state.printCSVPercent(initialMoneyAmount) + "\n");
    }

    public int getStatesSize() {
        return states.size();
    }

    public String printHead() {
        return getTitle() + ";date;moneyPercent";
    }

    protected abstract String getTitle();

    public double computeMaxLossesPercent() {

        double losses = 0;
        for (int i = 0; i < states.size(); i++) {
            for (int j = i; j < states.size(); j++) {

                double current = (states.get(i).getMoney() - states.get(j).getMoney()) / initialMoneyAmount;
                if (current > losses)
                    losses = current;
            }
        }

        return Round.toMoneyAmount(losses * 100);
    }

    public double computeMaxMoneyPercent() {

        double maxes = 0;
        for (MoneyState state : states)
            if (state.getMoneyPercent(initialMoneyAmount) > maxes)
                maxes = state.getMoneyPercent(initialMoneyAmount);

        return maxes;
    }

    public double computeEndPeriodMoneyPercent() {
        if (states.size() <= 0)
            return 0;

        return getLastState().getMoneyPercent(initialMoneyAmount);
    }
}
