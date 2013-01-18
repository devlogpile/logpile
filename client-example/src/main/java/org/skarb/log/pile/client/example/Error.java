package org.skarb.log.pile.client.example;

/**
 * User: skarb
 * Date: 17/01/13
 */
public abstract class Error {

    protected String message;
    protected String exception;
    protected String messageException;

    protected Error() {
        init();
    }

    protected void init(){}

    public Error(String message, String exception, String messageException) {
        this();
        this.message = message;
        this.exception = exception;
        this.messageException = messageException;
    }

    abstract void dolog() throws Exception;

    abstract String title();
}
