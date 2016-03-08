package protocols.orders.model;

/**
 * Created by dledenev on 08.03.2016.
 */
public abstract class QuikTransaction {

    private String classCode;
    private String security;
    private QuikAction action;
    private String type = "L";
    private QuikOperation operation;
    private Integer volume;
    private Double value;
    private Integer transactionId;

    public String buildQuikString() {
        return "CLASSCODE=" + classCode + ";" +
                "SECCODE=" + security + ";" +
                "ACTION=" + getAction() + ";" +
                "TYPE=" + type + ";" +
                "OPERATION=" + operation.getValue() + ";" +
                "QUANTITY=" + volume + ";" +
                "PRICE=" + value + ";" +
                "TRANS_ID=" + transactionId
    }

    protected abstract String getAction();
}
