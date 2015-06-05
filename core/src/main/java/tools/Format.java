package tools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

public class Format {

    public static long indexFor(DateTime date) {

        DateTime year = new DateTime(date.getYear(), 1, 1, 0, 0);
        int days = getWorkingDaysBetweenTwoDates(year, date);

        int minutesOfDay = date.getMillisOfDay() / (1000 * 60);

        return days * (14 * 60 - 10) + minutesOfDay;
    }

    public static <T> T[] copyFromEnd(T[] array, int depth) {
        return java.util.Arrays.copyOfRange(array, array.length - depth, array.length);
    }

    public static <T> List<T> copyFromEnd(List<T> list, int depth) {
        return list.subList(list.size() - depth, list.size());
    }

    public static String asString(DateTime date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
        return formatter.print(date);
    }

    public static DateTime asDate(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");

        return formatter.parseDateTime(date);
    }

    public static int getWorkingDaysBetweenTwoDates(DateTime startDate, DateTime endDate) {

        if (startDate.getMillis() >= endDate.getMillis())
            return 0;

        int workDays = 0;
        DateTime current = startDate;
        while (current.getMillis() < endDate.getMillis()) {
            current = current.plusDays(1);

            if (current.getDayOfWeek() != Calendar.SATURDAY && current.getDayOfWeek() != Calendar.SUNDAY)
                workDays++;
        }

        return workDays;
    }
}
