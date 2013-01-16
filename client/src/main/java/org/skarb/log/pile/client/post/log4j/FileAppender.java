package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.spi.LoggingEvent;

/**
 * User: skarb
 * Date: 16/01/13
 */
public class FileAppender extends org.apache.log4j.FileAppender {

    @Override
    public void append(LoggingEvent event) {
        super.append(event);
        Log4JUtils.doLogpileCall(event, getErrorHandler());
    }
}
