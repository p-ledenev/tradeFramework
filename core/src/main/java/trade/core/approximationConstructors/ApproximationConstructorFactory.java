package trade.core.approximationConstructors;

/**
 * Created by ledenev.p on 05.05.2015.
 */
public class ApproximationConstructorFactory {

    public static IApproximationConstructor createConstructor() {
        return new LinearApproximationConstructor();
    }
}
