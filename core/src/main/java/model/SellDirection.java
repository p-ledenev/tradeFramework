package model;

/**
 * Created by DiKey on 04.04.2015.
 */
public class SellDirection extends OrderDirection {
    @Override
    public String name() {
        return "S";
    }

    @Override
    public int sign() {
        return -1;
    }
}
