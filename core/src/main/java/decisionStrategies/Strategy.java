package decisionStrategies;

/**
 * Created by DiKey on 12.04.2015.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Strategy {
    String name();
}
