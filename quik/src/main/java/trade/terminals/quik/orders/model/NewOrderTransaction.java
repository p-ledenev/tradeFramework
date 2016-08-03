package trade.terminals.quik.orders.model;

import lombok.Setter;
import trade.core.model.Order;
import trade.terminals.quik.orders.dictionary.*;

/**
 * Created by ledenev.p on 10.03.2016.
 */

@Setter
public class NewOrderTransaction extends Transaction {

    private String type = "L";
    private Double value;
    private String account;

    public NewOrderTransaction(Order order, String classCode, Double value, String account) {
        super(order, classCode);
        this.value = value;
        this.account = account;
    }

    @Override
    protected void fillRequisites() throws Throwable {
        addRequisite("ACCOUNT", account);
        addRequisite("SECCODE", order.getSecurity());
        addRequisite("TYPE", type);
        addRequisite("OPERATION", Operation.findFor(order));
        addRequisite("QUANTITY", order.getVolume());
        addRequisite("PRICE", value.intValue());
    }

    @Override
    protected Action getAction() {
        return Action.NewOrder;
    }

    @Override
    protected void finalizeSuccessOrder() {
        order.executed();
    }

    @Override
    protected void onSuccessSubmission() {
        status = TransactionStatus.SubmissionSucceed;
    }
}
