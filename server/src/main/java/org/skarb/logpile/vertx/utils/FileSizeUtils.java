package org.skarb.logpile.vertx.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utils class for calculate the rolling file size.
 * User: skarb
 * Date: 14/01/13
 */
public class FileSizeUtils {

    static final long KB_COEFFICIENT = 1024;
    static final long MB_COEFFICIENT = 1024 * KB_COEFFICIENT;
    static final long GB_COEFFICIENT = 1024 * MB_COEFFICIENT;
    private final static String LENGTH_PART = "([0-9]+)";
    private final static int DOUBLE_GROUP = 1;
    private final static String UNIT_PART = "(|k|m|g)s?";
    private final static int UNIT_GROUP = 2;
    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile(LENGTH_PART
            + "\\s*" + UNIT_PART, Pattern.CASE_INSENSITIVE);

    private FileSizeUtils() {
    }

    public static long calculate(final String value, final long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        final Matcher matcher = FILE_SIZE_PATTERN.matcher(value);

        long coefficient = 1L;
        if (matcher.matches()) {
            String lenStr = matcher.group(DOUBLE_GROUP);
            String unitStr = matcher.group(UNIT_GROUP);

            long lenValue = Long.valueOf(lenStr);

            switch (unitStr.toLowerCase()) {
                case "k":
                    coefficient = KB_COEFFICIENT;
                    break;
                case "m":
                    coefficient = MB_COEFFICIENT;
                    break;
                case "g":
                    coefficient = GB_COEFFICIENT;
                    break;
                default:
            }

            return lenValue * coefficient;
        } else {
            throw new IllegalArgumentException("String value [" + value
                    + "] is not in the expected format.");
        }
    }
}
