package decisionStrategies;

import averageConstructors.IAveragingSupport;
import lombok.Data;

/**
 * Created by DiKey on 12.04.2015.
 */

@Data
public class Derivative implements IAveragingSupport {

    private double value;

    public Derivative(double value) {
        this.value = value;
    }

    @Override
    public double getValueForAveraging() {
        return value;
    }
}
