package org.skarb.log.pile.client.post.util.log;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created with IntelliJ IDEA.
 * User: skarb
 * Date: 30/12/12
 */
class TestFilter implements Filter {

    public boolean isLoggable(LogRecord record) {
        return true;
    }
}
