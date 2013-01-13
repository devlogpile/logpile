package org.skarb.logpile.vertx.event.format;

import org.skarb.logpile.vertx.event.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * User: skarb
 * Date: 10/01/13
 */
public class FormatterUtils {
    public static final String DEFAULT_FORMAT = "yyyy/MM/dd HH:mm:ss SSS";
    public static final String DEFAULT_SEPARATOR = " - ";

    static String formatDate(final String format, final Date date) {
        String string = null;
        if (date != null) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            string = simpleDateFormat.format(date);
        }
        return Objects.toString(string, "");
    }

    static StringBuilder format(final String separator, final String format, final Event event) {
        final String application = Objects.toString(event.getApplication(), "");
        final String component = Objects.toString(event.getComponent(), "");
        final String message = Objects.toString(event.getMessage(), "");
        final String stacktrace = Objects.toString(event.getStackTrace(), "");


        final StringBuilder line = new StringBuilder(formatDate(format, event.toDate())).append(separator).append(application)
                .append(separator).append(component).append(separator).append(message);
        if (!"".equals(stacktrace)) {
            line.append("\n").append(stacktrace);
        }
        return line;
    }
}
