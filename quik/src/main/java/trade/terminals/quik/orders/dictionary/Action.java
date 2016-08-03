package trade.terminals.quik.orders.dictionary;

import lombok.Getter;

/**
 * Created by pledenev on 08.03.2016.
 */
public enum Action {

    NewOrder("NEW_ORDER"), KillOrder("KILL_ORDER");

    @Getter
    private String value;

    private Action(String value) {
        this.value = value;
    }
}
