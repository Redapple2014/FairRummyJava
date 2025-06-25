package org.fcesur.gcs.utility;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class CommonUtils {

    public static long parseLong(String str) {
        if (!StringUtils.isEmpty((String) str)) {
            try {
                return Long.parseLong(str);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public static long parseLongFromList(List<String> list, int index) {
        if (list != null && !list.isEmpty() && index > -1) {
            try {
                return Long.parseLong(list.get(index));
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public static int parseInt(String str) {
        if (!StringUtils.isEmpty((String) str)) {
            try {
                return Integer.parseInt(str);
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public static boolean isInteger(String str) {
        if (!StringUtils.isEmpty(str)) {
            try {
                Integer.parseInt(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static boolean parseBoolean(String str) {
        if (!StringUtils.isEmpty((String) str)) {
            try {
                return Boolean.valueOf(str);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static int parseIntFromList(List<String> list, int index) {
        if (list != null && !list.isEmpty() && index > -1) {
            try {
                return Integer.parseInt(list.get(index));
            } catch (Exception e) {
                return -1;
            }
        }
        return -1;
    }

    public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length = " + length);
        }
        int size = source.size();
        if (size <= 0) {
            return Stream.empty();
        }
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1)
              .mapToObj(n4 -> source.subList(n4 * length, n4 == fullChunks ? size : (n4 + 1) * length));
    }

    public static String dateToString(long time) {
        return CommonUtils.dateToString(time, "dd-MM-yyyy HH:mm:ss.SSS");
    }

    public static String dateToString(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    public static long stringToDate(String dateStr) {
        return CommonUtils.stringToDate(dateStr, "dd-MM-yyyy HH:mm:ss.SSS");
    }

    public static long stringToDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String changingTimeFormat(Timestamp date) {
        String formattedDate = "";
        try {
            final SimpleDateFormat _trnmtListDateFormatter = new SimpleDateFormat("yyMMddHHmm");
            if (date != null) {
                formattedDate = _trnmtListDateFormatter.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String formatDecimal(double value) {
        return formatDecimal(value, "#0.00");
    }

    public static Double formatDecimalWithRoundingHalfUp(double value) {
        NumberFormat twoDForm = new DecimalFormat("#0.00");
        twoDForm.setRoundingMode(RoundingMode.HALF_UP);
        return Double.valueOf(twoDForm.format(value));

    }

    public static String formatDecimal(double value, String format) {
        NumberFormat twoDForm = new DecimalFormat(format);
        return twoDForm.format(value);
    }

    public static Date addDays(Timestamp ts, int days) {
        Calendar startTimeCal = Calendar.getInstance();
        startTimeCal.setTime(new Date(ts.getTime()));
        startTimeCal.add(Calendar.DATE, days);
        return startTimeCal.getTime();
    }

    static int isConsecutive(String str) {
        // variable to store starting number
        int start;

        // length of the input String
        int length = str.length();

        // find the number till half of the String
        for (int i = 0; i < length / 2; i++) {

            // new String containing the starting
            // substring of input String
            String new_str = str.substring(0, i + 1);

            // converting starting substring into number
            int num = Integer.parseInt(new_str);

            // backing up the starting number in start
            start = num;

            // while loop until the new_String is
            // smaller than input String
            while (new_str.length() < length) {

                // next number
                num++;

                // concatenate the next number
                new_str = new_str + String.valueOf(num);
            }

            // check if new String becomes equal to
            // input String
            if (new_str.equals(str))
                return start;
        }

        // if String doesn't contains consecutive numbers
        return -1;
    }

    public static <K, V> boolean isMapEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static int getSizeOf(Map<?, ?> mapObject) {
        if (mapObject == null) {
            return -1;
        }

        return mapObject.size();
    }

    public static int getSizeOf(Collection<?> col) {
        if (col == null) {
            return -1;
        }

        return col.size();
    }

    public static List<?> sampleOfCollection(List<?> input, int sampleSize) {
        if (input == null) {
            return null;
        }

        if (input.size() <= sampleSize) {
            return input;
        }

        return input.subList(0, sampleSize - 1);
    }

    public static List<?> sampleOfCollection(Set<?> input, int sampleSize) {
        return sampleOfCollection((new ArrayList<Object>(input)), sampleSize);
    }

    private static double getDoubleValue(String points) {
        if (StringUtils.isEmptyOrNull(points)) {
            return 0.0;
        }
        return Double.parseDouble(points);
    }
}
