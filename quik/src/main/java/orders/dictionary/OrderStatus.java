package orders.dictionary;

/**
 * Created by pledenev on 07.03.2016.
 */
public enum OrderStatus {

    Active(1), Cancelled(2), Done(3);

    private int value;

    public OrderStatus getBy(Long code) {
        for (OrderStatus status : OrderStatus.values())
            if (status.hasValue(code))
                return status;

        return Done;
    }

    private boolean hasValue(Long code) {
        return value == code.intValue();
    }

    private OrderStatus(int value) {
        this.value = value;
    }
}
