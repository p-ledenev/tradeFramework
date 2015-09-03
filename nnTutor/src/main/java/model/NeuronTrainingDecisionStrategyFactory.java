package model;

import decisionStrategies.neuron.ApproximationNeuronTrainingDecisionStrategy;
import decisionStrategies.neuron.NeuronTrainingDecisionStrategy;

/**
 * Created by DiKey on 23.08.2015.
 */
public class NeuronTrainingDecisionStrategyFactory {

    public static NeuronTrainingDecisionStrategy createStrategy() {
        return new ApproximationNeuronTrainingDecisionStrategy();
    }
}
