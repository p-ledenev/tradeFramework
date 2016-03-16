package protocols;

import lombok.*;
import model.Order;
import orders.callbacks.*;
import orders.dictionary.ResponseCode;
import orders.model.*;
import orders.requests.*;
import tools.Log;

import java.util.List;

/**
 * Created by pledenev on 05.03.2016.
 */
public class QuikTransactionsGateway {

	private TransactionsQueue queue;

	@Setter
	private QuikCandlesGateway candlesGateway;

	@Getter
	@Setter
	private volatile ResponseCode connectionStatus;

	@Setter
	private String classCode;
	@Setter
	private String pathToQuik;

	public QuikTransactionsGateway() {
		queue = new TransactionsQueue();
		connectionStatus = ResponseCode.Success;
	}

	public void submitTransactionsBy(List<Order> orders) {
		for (Order order : orders)
			submitTransactionBy(order);
	}

	public void drop(Transaction transaction) {
		Transaction droppedTransaction = KillOrderTransaction.by(transaction);
		submit(droppedTransaction);
	}

	public void submitTransactionBy(Order order) {

		double value = candlesGateway.loadLastValueFor(order.getSecurity());
		Transaction transaction = new NewOrderTransaction(order, classCode, value);

		submit(transaction);
	}

	private void submit(Transaction transaction) {
		queue.add(transaction);

		try {
			execute(new SubmitOrderRequest(transaction));
			transaction.submitted();
		} catch (Throwable e) {
			transaction.submissionFailed();
		}
	}

	public void registerCallbacks() throws Throwable {
		registerSubmissionCallback();
		registerExecutionCallback();
		registerConnectionCallback();

		Log.info("All callbacks registered");
	}

	private void registerExecutionCallback() throws Throwable {
		OrderStatusCallback statusCallback = new OrderStatusCallback(queue);
		RegisterOrderStatusCallbackRequest request = new RegisterOrderStatusCallbackRequest(statusCallback);

		execute(request);
	}

	private void registerSubmissionCallback() throws Throwable {
		TransactionReplyCallback replyCallback = new TransactionReplyCallback(queue);
		RegisterTransactionReplyCallbackRequest request = new RegisterTransactionReplyCallbackRequest(replyCallback);

		execute(request);
	}

	private void registerConnectionCallback() throws Throwable {
		ConnectionStatusCallback statusCallback = new ConnectionStatusCallback(this);
		RegisterConnectionStatusCallbackRequest request = new RegisterConnectionStatusCallbackRequest(statusCallback);

		execute(request);
	}

	// TODO might be moved to CandlesGateway
	public int loadSecurityVolume(String security) {
		return 0;
	}

	public void connect() throws Throwable {

		ConnectDllToTerminalRequest terminalConnection = new ConnectDllToTerminalRequest(pathToQuik);
		QuikResponse response = terminalConnection.execute();

		if (!response.isDllConnected())
			throw new Exception("Auto couldn't connect to Quik Terminal\n" + response.getErrorMessage());

		CheckTerminalConnectionRequest quikConnection = new CheckTerminalConnectionRequest();
		response = quikConnection.execute();

		if (!response.isQuikConnected())
			throw new Exception("Quik Terminal doesn't connected to server");
	}

	private QuikResponse execute(QuikRequest request) throws Throwable {

		QuikResponse response = request.execute();

		Log.info("Request " + request.getClass().getCanonicalName() + " executed");

		if (!response.isSuccess())
			throw new Exception(response.getErrorMessage());

		return response;
	}

	public boolean hasUnfinishedTransactions() {
		return queue.hasUnfinished();
	}

	public void dropUnfinishedTransactions() {
		for (Transaction transaction : queue.getTransactions())
			drop(transaction);
	}

	public void finalizeOrders() {
		queue.getTransactions().forEach(Transaction::finalizeOrder);
	}

	// TODO possible memory leak if callback (which has reference on queue) never be invoked?
	public void cleanOrdersQueue() {
		queue = new TransactionsQueue();
	}
}
