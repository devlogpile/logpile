package org.skarb.log.pile.client.example;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * User: skarb
 * Date: 17/01/13
 */
public class JavaUtilError extends Error {

    private static final Logger logger = Logger.getLogger(SwingTest.class.getName());

    public JavaUtilError() {
        super();
    }

    public JavaUtilError(String message, String exception, String messageException) {
        super(message, exception, messageException);
    }

    protected void init() {
        LogManager logMan = LogManager.getLogManager();

        try {
            logMan.readConfiguration(Thread.currentThread().getClass().getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dolog() throws Exception {
        if (exception == null || exception.trim().isEmpty()) {
            logger.severe(message);
        } else {
            Throwable t = null;
            if (messageException != null && !messageException.trim().isEmpty()) {
                t = (Throwable) Class.forName(exception).newInstance();
            } else {
                t = (Throwable) Class.forName(exception).getConstructor(String.class).newInstance(messageException);
            }

            logger.log(Level.SEVERE, message, t);
        }
    }

    @Override
    String title() {
        return " Java Util Logging";
    }
}
