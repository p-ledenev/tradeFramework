package decisionStrategies.neuron;

import decisionStrategies.*;
import decisionStrategies.algorithmic.*;
import lombok.*;
import org.encog.neural.networks.*;
import org.encog.persist.*;

import java.io.*;

/**
 * Created by ledenev.p on 21.08.2015.
 */

@Data
public abstract class NeuronDecisionStrategy extends DecisionStrategy {

    protected BasicNetwork network;
    protected AveragingDecisionStrategy averagingStrategy;

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
        return network.getInputCount() + 1;
    }

    public void readNetwork(String fileName) throws Throwable {
        network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(fileName));
    }

    public abstract String getNetworkName();

    @Override
    public boolean hasCurrentState() {
        return averagingStrategy.hasCurrentState();
    }
}
