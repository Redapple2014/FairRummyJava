package com.fairrummy.utility;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String DOT = ".";

    protected StringUtils() {
    }

    /**
     * Checks whether a given String has a valid content or not. Empty spaces
     * are not treated as a valid content for a String.
     *
     * @param str
     * @return true if the string is null or empty spaces.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().equals(""));
    }

    /**
     * Tokenises a String based on a delimiter and returns the tokens in an
     * Array.
     *
     * @param string
     * @param token
     * @return String Array
     */
    public static String[] toStringArray(String string, String token) {
        if (string == null || token == null)
            throw new IllegalArgumentException("String / Delimiter cannot be null");
        StringTokenizer tokenizer = new StringTokenizer(string, token);
        String[] stringArray = new String[tokenizer.countTokens()];
        int count = 0;
        while (tokenizer.hasMoreElements()) {
            stringArray[count++] = tokenizer.nextToken();
        }
        return stringArray;
    }

    /**
     * Tokenises a String based on the default delimiter(',') and returns the
     * tokens in an Array.
     *
     * @param string
     * @return String Array
     */
    public static String[] toStringArray(String string) {
        return toStringArray(string, ",");
    }

    /**
     * Converts a String to a char messageType The length of the String must not
     * be greates that one.
     *
     * @param string
     * @return character
     */
    public static char toChar(String string) {
        if (string == null || string.length() != 1)
            throw new IllegalArgumentException("String needs to have a single character");
        return string.charAt(0);
    }

    /**
     * Converts a char to a String
     *
     * @param c char
     * @return string
     */
    public static String toString(char c) {
        return new String(new char[]{c});
    }

    /**
     * Converts target string to uppercase
     *
     * @param target
     * @return converted string
     */
    public static String capitalize(String target) {
        if (isEmpty(target))
            return null;
        return Character.toUpperCase(target.charAt(0)) + target.substring(1);
    }

    /**
     * @param regex
     * @param targetString
     * @return The result of String.split(regex) except that Trailing empty
     * strings are included in the resulting array
     */
    public static String[] split(String regex, String targetString) {
        if (targetString != null) {
            if (targetString.endsWith(regex)) {
                if (!regex.equals("1"))
                    targetString += "1";
                else
                    targetString += "2";
                String[] result = targetString.split(regex);
                result[result.length - 1] = "";
                return result;
            } else {
                return targetString.split(regex);
            }
        } else {
            return null;
        }
    }

    /**
     * Returns a String with newline added between the content e.g.: if the
     * content is content={"Hi There, ", "I'm fine", "Thanks,"}, the returned
     * String would be:
     *
     * <pre>
     * Hi There,
     * I'm fine
     * Thanks,
     * </pre>
     *
     * @param content chunks of text that need to be separated by newline
     * @return result string
     */
    public static String formatTextWithNewLine(String[] content) {
        try {
            StringWriter sw = new StringWriter();
            BufferedWriter bf = new BufferedWriter(sw);
            for (int i = 0; i < content.length; i++) {
                bf.write(content[i]);
                bf.newLine();
            }
            bf.close();
            String formattedText = sw.getBuffer().toString();
            return formattedText;
        } catch (IOException ioe) {
            return EMPTY_STRING;
        }
    }

    public static String getStackTrace(Throwable e) {
        if (e == null)
            return EMPTY_STRING;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            e.printStackTrace(pw);
            pw.flush();
            e.printStackTrace();
        } finally {
            pw.close();
        }
        return sw.toString();
    }

    /**
     * Concatenates individual strings in the array, separated by the delimiter
     * string each time.
     *
     * @param array
     * @param delimiter
     * @return result string
     */
    public static String toDelimiterSeparatedString(String[] array, String delimiter) {
        StringBuilder expression = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (!isEmpty(array[i])) {
                expression.append(array[i]);
                expression.append(delimiter);
            }
        }
        // Delete the extra "'" character at the end
        if (expression.length() > 2)
            expression.deleteCharAt(expression.length() - 1);
        return expression.toString();
    }

    /**
     * Checks whether a string has digits
     *
     * @param s
     * @return true if s contains at least 1 digit
     */
    public static boolean hasDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i)))
                return true;
        }
        return false;
    }

    public static HashMap<String, String> stringToPropsMap(String propsStr) throws IOException {
        if (propsStr == null)
            return new HashMap<String, String>();
        Properties props = new Properties();
        ByteArrayInputStream bais = null;
        bais = new ByteArrayInputStream(propsStr.getBytes());
        props.load(bais);
        HashMap<String, String> map = new HashMap<String, String>();
        Enumeration<Object> en = props.keys();
        String key, value;
        while (en.hasMoreElements()) {
            key = (String) en.nextElement();
            value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }

    public static String propsMapToStr(Map<String, Object> props) {
        if (props == null || props.isEmpty())
            return "";
        Iterator<Map.Entry<String, Object>> itr = props.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            sb.append(entry.getKey());
            sb.append("=");
            Object val = entry.getValue();
            sb.append(val != null ? val.toString() : "null");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String collectionToCommaDelimitedString(Collection<?> collection) {
        if (collection != null && !collection.isEmpty()) {
            return org.springframework.util.StringUtils.collectionToCommaDelimitedString(collection);
        } else {
            return "";
        }
    }

    public static Set<String> commaDelimitedStringToCollection(String commaDelimitedStr) {
        if (!isEmpty(commaDelimitedStr)) {
            return org.springframework.util.StringUtils.commaDelimitedListToSet(commaDelimitedStr);
        } else {
            return Collections.emptySet();
        }
    }

    public static String concat(String... strings) {
        StringBuilder toReturn = new StringBuilder();
        for (String string : strings) {
            toReturn.append(string);
        }
        return toReturn.toString();
    }

    public static boolean isEmptyOrNull(Object obj) {
        return (obj == null) || (isEmpty(obj.toString()));
    }

    public static boolean checkArraysLengthAreEqual(Object... arrays) {
        if (arrays.length < 1) {
            return true;
        }
        int expectedLength = Array.getLength(arrays[0]);
        for (int i = 1; i < arrays.length; i++) {
            int length = Array.getLength(arrays[i]);
            if (length != expectedLength) {
                return false;
            }
        }
        return true;
    }

    public static boolean isIntegerAndBetween(String s, int start, int end) {
        try {
            int num = Integer.parseInt(s);
            if (num >= start && num <= end) {
                return true;
            }
            return false;

        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isOfLength(String str, int min, int max) {
        return !isEmpty(str) && str.length() >= min && str.length() <= max;
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false");
    }

    public static String removeLeadingZeros(String str) {
        return str.trim().replaceFirst("^0+(?!$)", "");
    }

    public static boolean in(String[] arr, String item) {
        return arr != null && arr.length != 0 && Arrays.stream(arr).anyMatch(item::equals);
    }

    public static boolean isIntegerAndBetween(int num, String start, String end) {
        try {
            if (num >= Integer.parseInt(start) && num <= Integer.parseInt(end)) {
                return true;
            }
            return false;

        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isValueInMap(Map<String, Object> params, String key) {
        if (params == null || params.isEmpty() || isEmpty(key))
            return false;

        Object result = params.get(key);
        if (result == null)
            return false;

        return true;
    }

    public static boolean containsIllegalChars(String toExamine) {
        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }

    public static boolean matchesPattern(String pattern, String s) {
        return s.matches(pattern);
    }

    public static String concat(String delimiter, Object... args) {
        final int length = args.length;
        if (length <= 0)
            return null;

        StringBuilder builder = new StringBuilder();
        builder.append(args[0]);
        if (length > 1) {
            for (int i = 1; i < length; i++) {
                builder.append(delimiter).append(args[i]);
            }
        }
        return builder.toString();
    }

    /**
     * Method to encode string to Base64 encoding
     *
     * @param toEncode String
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String base64EncodedString(String toEncode) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(toEncode.getBytes("utf-8"));
    }

    /**
     * Method to decode Base64 encoded string
     *
     * @param toDecode String
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String base64DecodeString(String toDecode) throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(toDecode.getBytes("utf-8")));
    }


    public static boolean isStringValid(String toExamine) {
        /**We are doing regex matching for the below characters
         * a-z : all small letters
         * A-Z : all capital letters
         * \\d : all digits
         * \\s : all spaces
         * +
         * &
         * ₹ : Rupee Symbol
         * ,
         * :
         * ||
         * |
         * #
         * .
         * *
         * -
         * @
         * / : for paths
         * */
        Pattern pattern = Pattern.compile("[a-zA-Z\\d\\s,%₹&$+*|:#.\\-@/]+");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.matches();
    }
}

