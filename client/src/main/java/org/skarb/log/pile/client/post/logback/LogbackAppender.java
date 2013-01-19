package org.skarb.log.pile.client.post.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.skarb.log.pile.client.post.engine.Engine;

/**
 * Appender which do only the registering events.
 * User: skarb
 * Date: 11/01/13
 */
public class LogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    /**
     * The current engine implemenation.
     */
    private Engine engine;
    /**
     * the application name.
     */
    private String application;

    /**
     * Default constructor.
     */
    public LogbackAppender() {
        super();
    }

    /**
     * Constructor user for testing.
     *
     * @param engine the engine implementation.
     * @param application the application name.
     */
    LogbackAppender(final Engine engine, final String application) {
        this.engine = engine;
        this.application = application;
    }

    /**
     * Init the datas.
     */
    @Override
    public void start() {
        super.start();
        if (engine == null) {
            engine = LogbackUtils.engine();
        }
        if (application == null) {
            application = LogbackUtils.application();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(final ILoggingEvent eventObject) {

        LogbackUtils.doCallLogPile(eventObject, application, engine, this);
    }

    /**
     * Getter for the engine.
     * <p>Used for testing.</p>
     *
     * @return the engine.
     */
    Engine getEngine() {
        return engine;
    }

    /**
     * Getter for the application name.
     * <p>Used for testing.</p>
     *
     * @return the engine.
     */
    String getApplication() {
        return application;
    }
}
