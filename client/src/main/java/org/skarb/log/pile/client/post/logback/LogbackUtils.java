package org.skarb.log.pile.client.post.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.status.ErrorStatus;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.Calendar;

/**
 * Utils class for log back api.
 * User: skarb
 * Date: 19/01/13
 */
public final class LogbackUtils {

    /**
     * Default constructor.
     */
    private LogbackUtils() {

    }

    /**
     * Register the current event log on the logpile server.
     * @param eventObject   the current log.
     * @param contextAware  the appender which do the treatment.
     */
    public static void doCallLogPile(final ILoggingEvent eventObject, final ContextAware contextAware) {
        doCallLogPile(eventObject,serverId(), application(), engine(), contextAware);
    }

    /**
     * Register the current event log on the logpile server.
     * @param eventObject   the current log.
     * @param application   the application name.
     * @param engine        the current engine implementation.
     * @param contextAware  the appender which do the treatment.
     */
    public static void doCallLogPile(final ILoggingEvent eventObject,final String serverId,  final String application, final Engine engine, ContextAware contextAware) {
        try {
            final Event event = new Event();
            event.setServerId(serverId);
            event.setApplication(application);
            event.setComponent(eventObject.getLoggerName());

            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(eventObject.getTimeStamp());
            event.setDate(instance.getTime());
            event.setMessage(eventObject.getMessage());

            event.setStacktrace(createStackTrace(eventObject));

            engine.post(event);
        } catch (Exception e) {
            contextAware.addStatus(new ErrorStatus(
                    "Error in logpile treament.",
                    contextAware, e));
        }
    }

    /**
     * Specific Format for the Logback api.
     *
     * @param eventObject the log.
     * @return the formatted stackTrace. If no exception, then this method return null.
     */
    private static String createStackTrace(final ILoggingEvent eventObject) {

        final IThrowableProxy tp = eventObject.getThrowableProxy();
        if (tp != null) {
            final StringBuilder stringBuilder = new StringBuilder(tp.getClassName()).append(": ").append(tp.getMessage()).append("\n");
            for (final StackTraceElementProxy stackTraceLine : tp.getStackTraceElementProxyArray()) {
                stringBuilder.append(stackTraceLine.toString()).append("\n");
            }

            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * Getter on the current engine implementation.
     * @return   the current engine implementation.
     */
    public static Engine engine() {
        final JavaUtilLogData logData = JavaUtilLogData.getInstance();
        return logData.getEngine();

    }

    /**
     * Getter on the  current application name.
     * @return   the  current application name.
     */
    public static String application() {
        final JavaUtilLogData logData = JavaUtilLogData.getInstance();
        return logData.getApplication();

    }

    /**
     * Getter on the  current application name.
     * @return   the  current application name.
     */
    public static String serverId() {
        final JavaUtilLogData logData = JavaUtilLogData.getInstance();
        return logData.getServerId();

    }
}
