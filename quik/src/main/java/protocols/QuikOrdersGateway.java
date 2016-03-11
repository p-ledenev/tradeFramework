package protocols;

import lombok.Setter;
import model.Order;
import orders.callbacks.*;
import orders.model.*;
import orders.requests.*;

/**
 * Created by dlede on 05.03.2016.
 */
public class QuikOrdersGateway {

	private TransactionsQueue queue;

	@Setter
	private String classCode;
	@Setter
	private String pathToQuik;

	public QuikOrdersGateway() {
		queue = new TransactionsQueue();
	}

	public void delete(int orderId) {

	}

	public void submit(Order order) throws Throwable {

		Transaction transaction = new NewOrderTransaction(order, classCode);
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
	}

	private void registerExecutionCallback() throws Throwable {
		OrderStatusCallback statusCallback = new OrderStatusCallback(this);
		RegisterOrderStatusCallbackRequest request = new RegisterOrderStatusCallbackRequest(statusCallback);

		execute(request);
	}

	private void registerSubmissionCallback() throws Throwable {
		TransactionReplyCallback replyCallback = new TransactionReplyCallback(this);
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

		CheckTerminalConnectionRequest quikConnection = new CheckTerminalConnectionRequest();
		QuikResponse response = quikConnection.execute();

		if (!response.isQuikConnected())
			throw new Exception("Quik Terminal doesn't connected to server");

		ConnectDllToTerminalRequest terminalConnection = new ConnectDllToTerminalRequest(pathToQuik);
		response = terminalConnection.execute();

		if (!response.isDllConnected())
			throw new Exception("Auto couldn't connect to Quik Terminal\n" + response.getErrorMessage());
	}

	private QuikResponse execute(QuikRequest request) throws Throwable {

		QuikResponse response = request.execute();

		if (!response.isSuccess())
			throw new Exception(response.getErrorMessage());

		return response;
	}

	public Transaction findTransactionBy(int id) throws TransactionNotFound {
		return queue.findBy(id);
	}
}
