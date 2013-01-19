package org.skarb.log.pile.client.post.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Appender which do registering errors and file writing.
 * User: skarb
 * Date: 19/01/13
 */
public class FileAppender extends ch.qos.logback.core.FileAppender<ILoggingEvent> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(final ILoggingEvent eventObject) {
        super.append(eventObject);
        LogbackUtils.doCallLogPile(eventObject, this);
    }
}
