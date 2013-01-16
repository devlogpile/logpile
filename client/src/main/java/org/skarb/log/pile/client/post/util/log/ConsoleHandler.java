package org.skarb.log.pile.client.post.util.log;

import java.util.logging.LogRecord;

/**
 * Handler which call the web registering server error and do console output.
 * User: skarb
 * Date: 16/01/13
 */
public class ConsoleHandler extends java.util.logging.ConsoleHandler {



    public ConsoleHandler() {
        super();
    }

    @Override
    public void publish(final LogRecord record) {
        super.publish(record);

        // call ws rest
        if (!isLoggable(record)) {
            return;
        }
        try {
            JavaUtilLogUtils.doCallLogPile(record, getFormatter());


        } catch (final Exception e) {
            reportError(JavaUtilLogUtils.MESSAGE_ERROR, e, JavaUtilLogUtils.TYPE_OF_FAILURE);
        }
    }
}
