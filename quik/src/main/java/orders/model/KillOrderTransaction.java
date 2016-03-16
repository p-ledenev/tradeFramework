package orders.model;

import lombok.Setter;
import model.Order;
import orders.dictionary.Action;

/**
 * Created by ledenev.p on 10.03.2016.
 */

@Setter
public class KillOrderTransaction extends Transaction {

	public static Transaction by(Transaction transaction) {
		KillOrderTransaction droppedTransaction = new KillOrderTransaction(transaction.order, transaction.classCode);
		droppedTransaction.setSourceTransactionId(transaction.id);

		return droppedTransaction;
	}

	private Integer sourceTransactionId;

	public KillOrderTransaction(Order order, String classCode) {
		super(order, classCode);
	}

	@Override
	protected Action getAction() {
		return Action.KillOrder;
	}

	@Override
	protected void finalizeSuccessOrder() {
		// order.unblock();
	}

	@Override
	protected void fillRequisites() {
		addRequisite("ORDER_KEY", sourceTransactionId);
	}
}
