package protocols.orders.model;

import lombok.Getter;

/**
 * Created by dlede on 08.03.2016.
 */
public enum QuikOperation {

    Buy("B"), Sell("S");

    @Getter
    private String value;

    private QuikOperation(String value) {
        this.value = value;
    }
}
