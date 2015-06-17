package exceptions;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public class OrderSubmissionFailure extends Exception {

    public OrderSubmissionFailure(String message) {
        super(message);
    }
}
