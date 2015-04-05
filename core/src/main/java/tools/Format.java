package tools;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Format {

    public static long indexFor(DateTime date) {

        DateTime year = new DateTime(date.getYear(), 1, 1, 0, 0);

        return (date.getMillis() - year.getMillis()) / (1000 * 60);
    }
}
