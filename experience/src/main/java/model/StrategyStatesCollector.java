package model;

import decisionStrategies.DecisionStrategy;
import decisionStrategies.StrategyState;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledenev.p on 14.05.2015.
 */
@Data
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

    public StrategyState getLastState() {
        return get(states.size() - 1);
    }

    public StrategyState get(int i) {
        if (states.size() <= i)
            return null;

        return states.get(i);
    }

    public int collectionSize() {
        return states.size();
    }

    public String printHeader() {

        String response = "date;dateIndex;value;";
        for (String param : strategy.getStateParamsHeader())
            response += param + ";";

        return response;
    }
}
