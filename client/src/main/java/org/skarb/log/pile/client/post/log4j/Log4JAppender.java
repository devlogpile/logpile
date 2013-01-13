package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.Date;

public class Log4JAppender extends AppenderSkeleton {
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
     * Null implementation.
     */
    @Override
    public void close() {

    }

    /**
     *
     */
    @Override
    public boolean requiresLayout() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     *
     */
    @Override
    protected void append(LoggingEvent loggingEvent) {

        JavaUtilLogData logData = JavaUtilLogData.getInstance();

        try {
            final Engine engine = logData.getEngine();
            final Event event = new Event();
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
            getErrorHandler().error("error in logpile", e,
                    ErrorCode.GENERIC_FAILURE);
        }


    }

}
