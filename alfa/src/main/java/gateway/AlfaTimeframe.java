package gateway;

/**
 * Created by ledenev.p on 31.03.2015.
 */
public enum AlfaTimeframe {

    minute(0), minutes5(1),minutes10(2), minutes15(3), minutes30(4), hour(5), day(6), week(7), month(8), year(9);

    int code;

    AlfaTimeframe(int code) {
        this.code = code;
    }
}