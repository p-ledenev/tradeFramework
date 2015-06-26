package decisionStrategies;

import averageConstructors.*;
import lombok.*;

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
