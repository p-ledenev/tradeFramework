package decisionStrategies.neuron;

import decisionStrategies.*;
import org.encog.neural.networks.*;

/**
 * Created by ledenev.p on 21.08.2015.
 */
public abstract class NeuronDecisionStrategy extends DecisionStrategy {

    protected BasicNetwork network;

    @Override
    public String[] getStateParamsHeader() {
        return new String[0];
    }

    @Override
    protected String[] collectCurrentStateParams() {
        return new String[0];
    }

    @Override
    public int getInitialStorageSizeFor(int depth) {
        return network.getInputCount();
    }
}
