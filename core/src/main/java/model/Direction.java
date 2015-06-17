package model;

import lombok.Getter;

/**
 * Created by ledenev.p on 31.03.2015.
 */

public enum Direction {

    buy("B", 1), sell("S", -1), neutral("N", 0);

    @Getter
    private String name;
    @Getter
    private int sign;

    Direction(String name, int sign) {
        this.name = name;
        this.sign = sign;
    }

    public Direction opposite() {
        if (this.equals(buy))
            return sell;

        if (this.equals(sell))
            return buy;

        return neutral;
    }

    public boolean isOppositeTo(Direction direction) {
        if (buy.equals(this) && sell.equals(direction))
            return true;

        if (sell.equals(this) && buy.equals(direction))
            return true;

        return false;
    }
}
