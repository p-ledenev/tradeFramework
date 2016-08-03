package trade.core.averageConstructors;

/**
 * Created by DiKey on 25.04.2015.
 */
public class AverageConstructorFactory {

    public static IAverageConstructor createConstructor() {
        return new ExponentialAverageConstructor();
    }
}
