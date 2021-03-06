package trade.tryOut.model;

import trade.core.decisionStrategies.*;
import lombok.*;

import java.util.*;

/**
 * Created by ledenev.p on 14.05.2015.
 */
@Getter @Setter
public class StrategyStatesCollector {

    private List<StrategyState> states;
    private DecisionStrategy strategy;

    public StrategyStatesCollector(DecisionStrategy strategy) {
        this.strategy = strategy;
        states = new ArrayList<StrategyState>();
    }

    public void add(StrategyState state) {
        states.add(state);
    }

    public void addCurrentStateIfChanged() {
        if (!strategy.getCurrentState().equals(getLastState()))
            addCurrentState();
    }

    public void addCurrentState() {
        if (strategy.hasCurrentState())
            add(strategy.getCurrentState());
    }

    public StrategyState getLastState() {
        return get(states.size() - 1);
    }

    public StrategyState get(int i) {
        if (states.size() <= i || i < 0)
            return null;

        return states.get(i);
    }

    public int collectionSize() {
        return states.size();
    }

    public String printHeader() {

        String response = "dateIndex;date;value;";
        for (String param : strategy.getStateParamsHeader())
            response += param + ";";

        return response;
    }
}
