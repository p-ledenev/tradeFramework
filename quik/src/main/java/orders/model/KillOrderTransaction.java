package orders.model;

import lombok.Setter;
import orders.dictionary.Action;

/**
 * Created by ledenev.p on 10.03.2016.
 */

@Setter
public class KillOrderTransaction extends Transaction {

	private Integer sourceTransactionId;

	@Override
	protected Action getAction() {
		return Action.KillOrder;
	}

	@Override
	protected void fillRequisites() {
		addRequisite("ORDER_KEY", sourceTransactionId);
	}
}
