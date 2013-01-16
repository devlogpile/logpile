package org.skarb.log.pile.client.post.util.log;

import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;
import org.skarb.log.pile.client.util.LogpileException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Utils for Java Util Logging Handler.
 * User: skarb
 * Date: 16/01/13
 */
public final class JavaUtilLogUtils {
    /**
     * Error message.
     */
    public static final String MESSAGE_ERROR = "LogPile handler failure";
    /**
     * Type of failure for reporting an error.
     */
    public static final int TYPE_OF_FAILURE = ErrorManager.GENERIC_FAILURE;

    /**
     * Method for formatting the stackTrace.
     *
     * @param exception the exception of the stacktrace.
     * @return the stackTrace Value.
     */
    public static String formatStackTrace(final Throwable exception) {
        String throwable = "";
        if (exception != null) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            pw.println();
            exception.printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        return throwable;
    }

    /**
     * format the service name.
     *
     * @param record the source object.
     * @return the class name or the loggerName.
     */
    private static String formatService(final LogRecord record) {
        String source;
        if (record.getSourceClassName() != null) {
            source = record.getSourceClassName();
            if (record.getSourceMethodName() != null) {
                source += " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }
        return source;
    }

    /**
     * Calle the ws rest service.
     *
     * @param record the log datas.
     * @param formatter the formatter of the logger.
     * @throws LogpileException
     */
    public static void doCallLogPile(final LogRecord record, final Formatter formatter) throws LogpileException {
        JavaUtilLogData logData = JavaUtilLogData.getInstance();

        final Engine engine = logData.getEngine();
        final Event event = new Event();
        event.setApplication(logData.getApplication());
        event.setDate(new Date(record.getMillis()));
        event.setComponent(formatService(record));
        event.setMessage(formatter.formatMessage(record)
        );
        event.setStacktrace(formatStackTrace(record.getThrown()));

        engine.post(event);
    }
}
