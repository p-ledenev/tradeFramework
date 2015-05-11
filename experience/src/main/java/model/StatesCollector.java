package model;

import lombok.Getter;
import tools.Format;
import tools.Round;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiKey on 11.05.2015.
 */
public abstract class StatesCollector<TEntity extends IStateSupport> {

    @Getter
    protected TEntity entity;
    @Getter
    protected List<State> states;

    public StatesCollector(TEntity entity) {
        this.entity = entity;

        states = new ArrayList<State>();
    }

    public void addStateIfChanged() {

        if (entity.getState().equals(getLastState()))
            return;

        states.add(entity.getState());
    }

    protected State getLastState() {
        if (states.size() == 0)
            return null;

        return states.get(states.size() - 1);
    }

    public int getYear() {
        return getLastState().getDate().getYear();
    }

    public String printState(int index) {
        if (states.size() <= index)
            return ";;;";

        return ";" + states.get(index).printCSV();
    }

    public void writeStatesTo(PrintWriter writer) {
        for (State state : states)
            writer.write(";" + state.printCSV());
    }

    public int getStatesSize() {
        return states.size();
    }

    public String printHead() {
        return getTitle() + ";date;money";
    }

    protected abstract String getTitle();

    public double computeMaxLosses() {

        double losses = 0;
        for (int i = 0; i < states.size(); i++) {
            for (int j = i; j < states.size(); j++) {

                double current = (states.get(i).getMoney() - states.get(j).getMoney()) / states.get(i).getMoney();
                if (current > losses)
                    losses = current;
            }
        }

        return Round.toThree(losses);
    }

    public double computeMaxRelativeMoney() {

        double maxes = 0;
        for (State state : states)
            if (state.getMoney() > maxes)
                maxes = state.getMoney();

        return computeRelativeMoneyFor(maxes);
    }

    public double computeEndPeriodRelativeMoney() {
        if (states.size() <= 0)
            return 0;

        return computeRelativeMoneyFor(getLastState().getMoney());
    }


    protected double computeRelativeMoneyFor(double value) {
        if (states.size() <= 0)
            return 0;

        double beginMoneyValue = states.get(0).getMoney();

        return 100 + Round.toMoneyAmount((value - beginMoneyValue) / beginMoneyValue * 100);
    }
}
