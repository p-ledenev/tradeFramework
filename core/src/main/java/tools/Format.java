package tools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Format {

    public static long indexFor(DateTime date) {

        DateTime year = new DateTime(date.getYear(), 1, 1, 0, 0);

        return (date.getMillis() - year.getMillis()) / (1000 * 60);
    }

    public static <T> T[] copyFromEnd(T[] array, int depth) {
        return java.util.Arrays.copyOfRange(array, array.length - depth, array.length);
    }

    public static String asString(DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        return formatter.print(date);
    }
}
