package org.skarb.logpile.vertx.event.format;

import org.skarb.logpile.vertx.event.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Utils class for formatting event.
 * User: skarb
 * Date: 10/01/13
 */
public class FormatterUtils {
    public static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss SSS";
    public static final String LOG_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final String DEFAULT_SEPARATOR = " - ";

    /**
     * Method for formatting a date object.
     *
     * @param format the format string
     * @param date   the date to format.
     * @return the formatted date or an empty string.
     */
    public static String formatDate(final String format, final Date date) {
        String string = null;
        if (date != null) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            string = simpleDateFormat.format(date);
        }
        return Objects.toString(string, "");
    }

    /**
     * Method for formatting an event.
     * <p>Transform an event to line</p>
     *
     * @param separator the separator string
     * @param format    the format of the date.
     * @param event     the event to format
     * @return a new string line.
     */
    static StringBuilder format(final String separator, final String format, final Event event) {
        final String application = Objects.toString(event.getApplication(), "");
        final String component = Objects.toString(event.getComponent(), "");
        final String message = Objects.toString(event.getMessage(), "");
        final String stackTrace = Objects.toString(event.getStackTrace(), "");
        final String serverId = Objects.toString(event.getServerId(), "");


        final StringBuilder line = new StringBuilder(formatDate(format, event.toDate()));
        if (!"".equals(serverId)) {
            line.append(separator).append(serverId);
        }

        line.append(separator).append(application)
                .append(separator).append(component).append(separator).append(message);
        if (!"".equals(stackTrace)) {
            line.append("\n").append(stackTrace);
        }
        return line;
    }
}
