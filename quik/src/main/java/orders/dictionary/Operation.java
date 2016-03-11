package orders.dictionary;

import lombok.Getter;

/**
 * Created by pledenev on 08.03.2016.
 */
public enum Operation {

    Buy("B"), Sell("S");

    @Getter
    private String value;

    private Operation(String value) {
        this.value = value;
    }
}
