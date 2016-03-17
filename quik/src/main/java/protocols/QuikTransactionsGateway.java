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

@Setter
public class QuikTransactionsGateway {

    private TransactionsQueue queue;

    private QuikCandlesGateway candlesGateway;

    @Getter
    private volatile ResponseCode connectionStatus;

    // need to be declared here for correct callbacks calls
    private OrderStatusCallback orderStatusCallback;
    private ConnectionStatusCallback connectionStatusCallback;
    private TransactionReplyCallback transactionReplyCallback;


    private String classCode;
    private String account;

    private String pathToQuik;

    public QuikTransactionsGateway() {
        queue = new TransactionsQueue();
        connectionStatus = ResponseCode.Success;

        connectionStatusCallback = new ConnectionStatusCallback(this);

        orderStatusCallback = new OrderStatusCallback(queue);
        transactionReplyCallback = new TransactionReplyCallback(queue);
    }

    public void submitTransactionsBy(List<Order> orders) throws Throwable {
        for (Order order : orders)
            submitTransactionBy(order);
    }

    public void drop(Transaction transaction) throws Throwable {
        Transaction droppedTransaction = KillOrderTransaction.by(transaction);
        submit(droppedTransaction);
    }

    public void submitTransactionBy(Order order) throws Throwable {

        double value = candlesGateway.loadLastValueFor(order.getSecurity());
        Transaction transaction = new NewOrderTransaction(order, classCode, value, account);

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

    public void registerExecutionCallback() throws Throwable {

        subscribeToOrders();

        RegisterOrderStatusCallbackRequest request = new RegisterOrderStatusCallbackRequest(orderStatusCallback);
        execute(request);
    }

    public void subscribeToOrders() throws Throwable {
        UnsubscribeOrdersRequest unsubscribeOrdersRequest = new UnsubscribeOrdersRequest();
        execute(unsubscribeOrdersRequest);

        SubscribeOrdersRequest subscribeOrdersRequest = new SubscribeOrdersRequest(classCode);
        execute(subscribeOrdersRequest);
    }

    private void registerSubmissionCallback() throws Throwable {
        RegisterTransactionReplyCallbackRequest request = new RegisterTransactionReplyCallbackRequest(transactionReplyCallback);
        execute(request);
    }

    private void registerConnectionCallback() throws Throwable {
        RegisterConnectionStatusCallbackRequest request = new RegisterConnectionStatusCallbackRequest(connectionStatusCallback);
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

        Log.info("Request " + request.getClass().getSimpleName() + " executed");

        if (!response.isSuccess())
            throw new Exception(response.getErrorMessage());

        return response;
    }

    public boolean hasUnfinishedTransactions() {
        return queue.hasUnfinished();
    }

    public void dropUnfinishedTransactions() throws Throwable {
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
