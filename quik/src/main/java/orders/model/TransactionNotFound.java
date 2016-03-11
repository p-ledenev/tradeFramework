package orders.model;

/**
 * Created by ledenev.p on 11.03.2016.
 */
public class TransactionNotFound extends Exception {

	public TransactionNotFound(Integer transactionId) {
		super("For id " + transactionId);
	}
}
