package orders.model;

import lombok.Setter;
import model.Order;
import orders.dictionary.*;

import javax.annotation.concurrent.ThreadSafe;
import java.util.*;

/**
 * Created by dledenev on 08.03.2016.
 */

@Setter
@ThreadSafe
public abstract class Transaction {

	protected Integer id;
	protected String classCode;
	protected Order order;
	protected Long terminalOrderNumber;

	private Map<String, Object> requisites;

	protected volatile TransactionStatus status;

	public Transaction(Order order, String classCode) {

		this.id = TransactionIdIterator.getNext();

		this.order = order;
		this.classCode = classCode;

		requisites = new HashMap<>();
		status = TransactionStatus.Newest;
	}

	public String buildQuikString() throws Throwable {
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

	protected abstract void fillRequisites() throws Throwable;

	protected abstract Action getAction();

	protected abstract void finalizeSuccessOrder();

	public synchronized void submitted() {
		status = TransactionStatus.Submitted;
	}

	public synchronized void submissionFailed() {
		status = TransactionStatus.SubmissionFailed;
	}

	public synchronized void executed(double value) {
		status = TransactionStatus.ExecutionSucceed;
		order.setValue(value);
	}

	public synchronized void executedPartly() {
		status = TransactionStatus.ExecutedPartly;
	}

	public synchronized void deletionSucceed() {
		status = TransactionStatus.DeletionSucceed;
	}

	public boolean isSubmissionSucceed() {
		return TransactionStatus.SubmissionSucceed.equals(status);
	}

	public boolean hasId(Integer id) {
		return this.id != null && this.id.equals(id);
	}

	public synchronized void submissionSucceed(long orderNumber) {
		terminalOrderNumber = orderNumber;
		onSuccessSubmission();
	}

	protected abstract void onSuccessSubmission();

	public boolean isExecutionSucceed() {
		return TransactionStatus.ExecutionSucceed.equals(status);
	}

	public boolean isFinished() {
		TransactionStatus status = this.status;

		return !TransactionStatus.Submitted.equals(status) &&
				!TransactionStatus.SubmissionSucceed.equals(status) &&
				!TransactionStatus.ExecutedPartly.equals(status);
	}

	public void finalizeOrder() {
		if (!isExecutionSucceed()) {
			order.block();
			return;
		}

		finalizeSuccessOrder();
	}

	public String print() {
		return "transaction id " + id + "; status " + status;
	}

	public boolean hasSameVolume(int volume) {
		return order.hasVolume(volume);
	}
}
