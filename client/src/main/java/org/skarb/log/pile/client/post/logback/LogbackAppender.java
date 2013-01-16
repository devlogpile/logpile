package org.skarb.log.pile.client.post.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.ErrorStatus;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.Calendar;

/**
 * User: skarb
 * Date: 11/01/13
 */
public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private Engine engine;
    private String application;

    public LogbackAppender() {
    }

    LogbackAppender(final Engine engine, final String application) {
        this.engine = engine;
        this.application = application;
    }

    @Override
    public void start() {
        super.start();
        final JavaUtilLogData logData = JavaUtilLogData.getInstance();
        if (engine == null) {
            engine = logData.getEngine();
        }
        if (application == null) {
            application = logData.getApplication();
        }
    }

    @Override
    protected void append(final ILoggingEvent eventObject) {

        try {
            final Event event = new Event();
            event.setApplication(application);
            event.setComponent(eventObject.getLoggerName());

            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(eventObject.getTimeStamp());
            event.setDate(instance.getTime());
            event.setMessage(eventObject.getMessage());

            event.setStacktrace(createStackTrace(eventObject));

            engine.post(event);
        } catch (Exception e) {
            addStatus(new ErrorStatus(
                    "Error in logpile treament.",
                    this, e));
        }
    }

    private String createStackTrace(final ILoggingEvent eventObject) {

        final IThrowableProxy tp = eventObject.getThrowableProxy();
        if (tp != null) {
            final StringBuilder stringBuilder = new StringBuilder(tp.getClassName()).append(": ").append(tp.getMessage());
            for (final StackTraceElementProxy stackTraceLine : tp.getStackTraceElementProxyArray()) {
                stringBuilder.append(stackTraceLine.toString()).append("\n");
            }

            return stringBuilder.toString();
        }
        return null;
    }

    Engine getEngine() {
        return engine;
    }

    String getApplication() {
        return application;
    }
}
