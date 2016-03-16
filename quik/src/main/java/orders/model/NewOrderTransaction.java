package orders.model;

import lombok.Setter;
import model.Order;
import orders.dictionary.*;

/**
 * Created by ledenev.p on 10.03.2016.
 */

@Setter
public class NewOrderTransaction extends Transaction {

	private String type = "L";
	private Double value;

	public NewOrderTransaction(Order order, String classCode, Double value) {
		super(order, classCode);
		this.value = value;
	}

	@Override
	protected void fillRequisites() throws Throwable {
		addRequisite("SECCODE", order.getSecurity());
		addRequisite("TYPE", type);
		addRequisite("OPERATION", Operation.findFor(order));
		addRequisite("QUANTITY", order.getVolume());
		addRequisite("PRICE", value);
	}

	@Override
	protected Action getAction() {
		return Action.NewOrder;
	}

	@Override
	protected void finalizeSuccessOrder() {
		order.executed();
	}
}
