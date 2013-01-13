package org.skarb.log.pile.client.util;

/**
 * User: skarb
 * Date: 11/01/13
 */
public class LogpileException extends Exception {

    public LogpileException(CharSequence message) {
        super(message.toString());
    }

    public LogpileException(CharSequence message, Throwable cause) {
        super(message.toString(), cause);
    }

    public LogpileException(Throwable cause) {
        super(cause);
    }
}
