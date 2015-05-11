package model;

import lombok.Getter;

/**
 * Created by ledenev.p on 31.03.2015.
 */

public enum OrderDirection {

    buy("B", 1), sell("S", -1), none("N", 0);

    @Getter
    private String name;
    @Getter
    private int sign;

    private OrderDirection(String name, int sign) {
        this.name = name;
        this.sign = sign;
    }

    public boolean isEqual(OrderDirection direction) {
        return name().equals(direction.name());
    }
}
