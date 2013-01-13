package org.skarb.log.pile.client.post.util.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Mock for Formatter.
 * User: skarb
 * Date: 30/12/12
 * Time: 11:47
 * To change this template use File | Settings | File Templates.
 */
class TestFormatter extends Formatter {

    public String format(LogRecord record) {
        return "";
    }
}
