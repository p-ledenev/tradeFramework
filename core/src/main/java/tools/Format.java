package tools;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Format {

	final static Pattern CardNumberPattern = Pattern.compile("((?<!\\d)[3-9]{1}\\d{3})-?(\\d{4})-?(\\d{2})(\\d{2})-?(\\d{4}(?!\\d))");

	static public String decimal(double value, String decimalSeparator, int decimalDigitsCount) {
		return String.format("%." + decimalDigitsCount + "f", value).replaceAll(",", decimalSeparator);
	}

	public static String date(Date date) {

		return date(date, "dd.MM.yyyy HH:mm:ss");
	}

	public static String date(Date date, String pattern) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String leftPadded(String string, String paddingChar, int expectedLength) {
		String result = string;

		while (result.length() < expectedLength)
			result = paddingChar + result;

		return result;
	}

	public static String rightPadded(String string, String paddingChar, int expectedLength) {
		String result = string;

		while (result.length() < expectedLength)
			result += paddingChar;

		return result;
	}

	public static String money(double amount) {
		return decimal(amount, ".", 2);
	}

	public static List<String> toCapitalizedWords(String sentenceString) {

		if (sentenceString == null)
			return new ArrayList<String>(0);

		String[] words = sentenceString.split(" ");

		ArrayList<String> result = new ArrayList<String>(words.length);

		for (String each : words) {
			if (each.isEmpty())
				continue;

			String eachWord = each.trim();
			String firstChar = eachWord.substring(0, 1);
			String rest = eachWord.substring(1, eachWord.length());

			result.add(firstChar.toUpperCase() + rest.toLowerCase());

		}

		return result;
	}

	public static String toCapitalizedWordsString(String sentenceString) {
		StringBuffer result = new StringBuffer();

		List<String> words = toCapitalizedWords(sentenceString);
		int i = 0;
		for (String each : words) {
			if (i > 0)
				result.append(" ");
			result.append(each);
			i++;
		}

		return result.toString();
	}

	public static String personNameToInitials(String personName) {
		StringBuffer result = new StringBuffer();

		List<String> words = toCapitalizedWords(personName);
		for (String each : words) {
			result.append(each.substring(0, 1)).append(".");
		}

		return result.toString();
	}

	public static String stringByMaskingCards(String string) {
		if (string == null)
			return string;

		return CardNumberPattern.matcher(string).replaceAll("$1-****-****-$5");
	}

	public static String stringByReplacingCR(String string) {
		return string.replaceAll("\n", " ");
	}

	public static String stringByHashWithSHA256(String string) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.update(string.getBytes());

		byte byteData[] = md.digest();

		return Hex.encodeHexString(byteData);
	}
}
