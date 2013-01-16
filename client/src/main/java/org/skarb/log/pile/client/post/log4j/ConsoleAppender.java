package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * Appender which do an console output and call logpile server.
 * User: skarb
 * Date: 16/01/13
 */
public class ConsoleAppender extends org.apache.log4j.ConsoleAppender {


    /**
     * {@inheritDoc}
     */
    @Override
    public void append(final LoggingEvent loggingEvent) {
        super.append(loggingEvent);
        Log4JUtils.doLogpileCall(loggingEvent, getErrorHandler());
    }

}
