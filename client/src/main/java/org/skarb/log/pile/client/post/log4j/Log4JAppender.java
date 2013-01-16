package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Logger which do just a call to the web service of registration of logpile server.
 */
public class Log4JAppender extends AppenderSkeleton {



    /**
     * Null implementation.
     */

    public void close() {

    }

    public Log4JAppender() {
        super();
        yellDescription();
    }

    public Log4JAppender(boolean isActive) {
        super(isActive);
        yellDescription();
    }

    /**
     * {@inheritDoc}
     */

    public boolean requiresLayout() {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void append(LoggingEvent loggingEvent) {
        Log4JUtils.doLogpileCall(loggingEvent, getErrorHandler());
    }

    private void yellDescription(){
        System.out.println("initialisation of the appender which call logpile server.");
    }
}
