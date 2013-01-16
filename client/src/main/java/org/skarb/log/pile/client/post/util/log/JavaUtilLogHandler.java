package org.skarb.log.pile.client.post.util.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Handler which call the web registering server error.
 */
class JavaUtilLogHandler extends Handler {


    /**
     * Constructor.
     */
    public JavaUtilLogHandler() {
        configure(MyLogManager.getInstance());
    }

    /**
     * Create the current logManager.
     * @param manager the decorator.
     */
    void configure(final MyLogManager manager) {

        String cname = getClass().getName();

        setLevel(manager.getLevelProperty(cname + ".level", Level.INFO));
        setFilter(manager.getFilterProperty(cname + ".filter", null));
        setFormatter(manager.getFormatterProperty(cname + ".format", new SimpleFormatter()));
        try {
            setEncoding(manager.getStringProperty(cname + ".encoding", null));
        } catch (Exception ex) {
            try {
                setEncoding(null);
            } catch (Exception ex2) {
                // doing a setEncoding with null should always work.
                // assert false;
            }
        }

    }

  

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        try {
           JavaUtilLogUtils.doCallLogPile(record, getFormatter());


        } catch (final Exception e) {
            reportError(JavaUtilLogUtils.MESSAGE_ERROR, e, JavaUtilLogUtils.TYPE_OF_FAILURE);
        }
    }



    /**
     * Null Implemntation
     */
    @Override
    public void flush() {

    }

    /**
     * Null Implemntation
     */
    @Override
    public void close() throws SecurityException {

    }


}
