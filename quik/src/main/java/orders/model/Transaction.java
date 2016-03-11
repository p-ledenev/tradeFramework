package orders.model;

import lombok.Setter;
import model.Order;
import orders.dictionary.*;

import java.util.*;

/**
 * Created by dledenev on 08.03.2016.
 */

@Setter
public abstract class Transaction {

	private Integer id;
	private String classCode;
	private Map<String, Object> requisites;
	private TransactionStatus status;

	private Order order;

	public Transaction(Order order, String classCode) {
		this.order = order;
		this.classCode = classCode;

		requisites = new HashMap<>();
		status = TransactionStatus.Newest;
	}

	public String buildQuikString() {
		fillRequisites();

		return "CLASSCODE=" + classCode + ";" +
				"TRANS_ID=" + id + ";" +
				"ACTION=" + getAction().getValue() + ";" +
				formatRequisites();
	}

	private String formatRequisites() {
		String query = "";
		for (String key : requisites.keySet())
			query += key + "=" + requisites.get(key) + ";";

		return query;
	}

	protected void addRequisite(String key, Object value) {
		requisites.put(key, value);
	}

	protected abstract void fillRequisites();

	protected abstract Action getAction();

	public void submitted() {
		status = TransactionStatus.Submitted;
	}

	public void submissionFailed() {
		status = TransactionStatus.SubmissionFailed;
	}

	public void executed() {
		status = TransactionStatus.ExecutionSucceed;
	}

	public void executedPartly() {
		status = TransactionStatus.ExecutedPartly;
	}

	public void deletionSucceed() {
		status = TransactionStatus.DeletionSucceed;
	}

	public boolean isSubmissionSucceed() {
		return status == TransactionStatus.SubmissionSucceed;
	}

	public boolean hasId(Integer id) {
		return this.id != null && this.id.equals(id);
	}
}
