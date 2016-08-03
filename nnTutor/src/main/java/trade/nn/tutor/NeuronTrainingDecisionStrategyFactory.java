package trade.nn.tutor;

import trade.core.decisionStrategies.neuronTraining.*;

/**
 * Created by DiKey on 23.08.2015.
 */
public class NeuronTrainingDecisionStrategyFactory {

    public static NeuronTrainingDecisionStrategy createStrategy() {
        //return new ApproximationNeuronTrainingDecisionStrategy();
        return new MaxMinNeuronTrainingDecisionStrategy();
    }
}