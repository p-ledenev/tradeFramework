package protocols.orders.model;

import lombok.Getter;

/**
 * Created by dledenev on 08.03.2016.
 */
public enum QuikAction {

    NewOrder("NEW_ORDER"), KillOrder("KILL_ORDER");

    @Getter
    private String value;

    private QuikAction(String value) {
        this.value = value;
    }
}
