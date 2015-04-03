package model;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public enum OrderDirection {
Buy, Sell, None;

    public boolean isBuy() {
        return this.equals(Buy);
    }

    public boolean isSell() {
        return this.equals(Sell);
    }

    public boolean isNone() {
        return this.equals(None);
    }
}
