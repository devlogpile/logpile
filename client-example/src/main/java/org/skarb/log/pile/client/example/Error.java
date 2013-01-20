package org.skarb.log.pile.client.example;

/**
 * User: skarb
 * Date: 17/01/13
 */
public abstract class Error {

    private String message;
    private String exception;
    private String messageException;

    protected Error() {
        init();
    }



    protected void init() {
    }

    abstract void dolog(final String mess) throws Exception;

    abstract void dolog(final String mess, final Throwable throwable) throws Exception;

    void dolog() throws Exception {
        if (exception == null || exception.trim().isEmpty()) {
            dolog(message);
        } else {
            Throwable t = null;
            if (messageException == null || messageException.trim().isEmpty()) {
                t = (Throwable) Class.forName(exception).newInstance();
            } else {
                t = (Throwable) Class.forName(exception).getConstructor(String.class).newInstance(messageException);
            }

            dolog(message, t);
        }
    }

    abstract String title();


    public void setMessage(String message) {
        this.message = message;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMessageException(String messageException) {
        this.messageException = messageException;
    }
}
