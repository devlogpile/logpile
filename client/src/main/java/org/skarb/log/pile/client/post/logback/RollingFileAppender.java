package org.skarb.log.pile.client.post.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Appender which do registering errors and rolling file writing.
 * User: skarb
 * Date: 19/01/13
 */
public class RollingFileAppender extends ch.qos.logback.core.rolling.RollingFileAppender<ILoggingEvent> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(ILoggingEvent eventObject) {
        super.append(eventObject);
        LogbackUtils.doCallLogPile(eventObject, this);
    }
}
