package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.LoggingEvent;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.Date;

/**
 * Utils class for Log4J Appenders.
 * User: skarb
 * Date: 16/01/13
 */
public class Log4JUtils {
    /**
     * Method which format the stackTrace.
     * @param throwableStrRep the stack of the exception.
     * @return  the formatted stack
     */
    private static String formatStackTrace(final String[] throwableStrRep) {

        final StringBuilder retour = new StringBuilder();
        if (throwableStrRep != null && throwableStrRep.length > 0) {
            for (String string : throwableStrRep) {
                retour.append(string).append("\n");
            }
        }
        return retour.toString();
    }

    /**
     * Call the logpile server for registering errors.
     * @param loggingEvent the log event of LOg4j
     * @param errorHandler the error handler for reporting the error.
     */
    static void doLogpileCall(final LoggingEvent loggingEvent, final ErrorHandler errorHandler) {
        final JavaUtilLogData logData = JavaUtilLogData.getInstance();

        try {
            final Engine engine = logData.getEngine();
            final Event event = new Event();
            event.setServerId(logData.getServerId());
            event.setApplication(logData.getApplication());
            event.setDate(new Date(loggingEvent.timeStamp));
            if (loggingEvent.getMessage() != null) {
                event.setMessage(String.valueOf(loggingEvent.getMessage()));
            }
            event.setComponent(loggingEvent.getLoggerName());
            event.setStacktrace(formatStackTrace(loggingEvent
                    .getThrowableStrRep()));

            engine.post(event);
        } catch (Exception e) {

            errorHandler.error("error in logpile", e,
                    ErrorCode.GENERIC_FAILURE);
        }
    }
}
