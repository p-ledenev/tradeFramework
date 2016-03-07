package protocols.orders;

/**
 * Created by dlede on 07.03.2016.
 */
public enum QuikOrderStatus {

    Active(1), Cancelled(2), Done(3);

    private int value;

    public QuikOrderStatus getBy(Long code) {
        for (QuikOrderStatus status : QuikOrderStatus.values())
            if (status.hasValue(code))
                return status;

        return Done;
    }

    private boolean hasValue(Long code) {
        return value == code.intValue();
    }

    private QuikOrderStatus(int value) {
        this.value = value;
    }
}
