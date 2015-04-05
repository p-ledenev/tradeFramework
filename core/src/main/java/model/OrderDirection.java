package model;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public abstract class OrderDirection {

    public abstract String name();

    public abstract int sign();

    public boolean isEqual(OrderDirection direction) {
        return name().equals(direction.name());
    }
}
