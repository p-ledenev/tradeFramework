package trade.terminals.quik.orders.model;

import java.util.*;

/**
 * Created by ledenev.p on 10.03.2016.
 */
public class TransactionsQueue {

	private List<Transaction> transactions;

	public TransactionsQueue() {
		transactions = new ArrayList<>();
	}

	public Transaction findBy(Integer id) throws TransactionNotFound {

		for (Transaction transaction : transactions)
			if (transaction.hasId(id))
				return transaction;

		throw new TransactionNotFound(id);
	}

	public void add(Transaction transaction) {
		transactions.add(transaction);
	}

	public void clean() {
		transactions = new ArrayList<>();
	}

	public boolean hasUnfinished() {

		for (Transaction transaction : transactions)
			if (!transaction.isFinished())
				return true;

		return false;
	}

	public void finalizeOrders() {
		transactions.forEach(Transaction::finalizeOrder);
	}

	public List<Transaction> getUnfinished() {
		List<Transaction> response = new ArrayList<>();

		for (Transaction transaction : transactions)
			if (!transaction.isFinished())
				response.add(transaction);

		return response;
	}
}
