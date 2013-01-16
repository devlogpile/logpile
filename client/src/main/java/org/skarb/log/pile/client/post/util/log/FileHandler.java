package org.skarb.log.pile.client.post.util.log;

import java.io.IOException;
import java.util.logging.LogRecord;

/**
 *  Handler which call the web registering server error and do file logging.
 * User: skarb
 * Date: 16/01/13
 */
public class FileHandler extends java.util.logging.FileHandler {

    public FileHandler() throws IOException, SecurityException {
        super();
    }

    public FileHandler(String pattern) throws IOException, SecurityException {
        super(pattern);
    }

    public FileHandler(String pattern, boolean append) throws IOException, SecurityException {
        super(pattern, append);
    }

    public FileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
        super(pattern, limit, count);
    }

    public FileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
        super(pattern, limit, count, append);
    }

    @Override()
    public synchronized void publish(LogRecord record) {
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
