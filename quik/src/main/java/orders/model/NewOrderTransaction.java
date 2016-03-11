package orders.model;

import lombok.Setter;
import model.Order;
import orders.dictionary.*;

/**
 * Created by ledenev.p on 10.03.2016.
 */

@Setter
public class NewOrderTransaction extends Transaction {

	private String security;
	private String type = "L";
	private Operation operation;
	private Integer volume;
	private Double value;

	public NewOrderTransaction(Order order, String classCode) {
		super(order, classCode);
	}

	@Override
	protected void fillRequisites() {
		addRequisite("SECCODE", security);
		addRequisite("TYPE", type);
		addRequisite("OPERATION", operation);
		addRequisite("QUANTITY", volume);
		addRequisite("PRICE", value);
	}

	@Override
	protected Action getAction() {
		return Action.NewOrder;
	}
}
