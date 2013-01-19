package org.skarb.log.pile.client.example;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;

/**
 * User: skarb
 * Date: 18/01/13
 */
public class Log4jError extends Error {

    private static final Logger logger = Logger.getLogger(Log4jError.class);

    public Log4jError() {
    }

    public Log4jError(String message, String exception, String messageException) {
        super(message, exception, messageException);
    }

    @Override
    protected void init() {
        URL resource = Thread.currentThread().getClass().getResource("/log4j.properties");
        PropertyConfigurator.configure(resource.getPath());
    }

    @Override
    void dolog() throws Exception {
        if (exception == null || exception.trim().isEmpty()) {
            logger.error(message);
        } else {
            Throwable t = null;
            if (messageException != null && !messageException.trim().isEmpty()) {
                t = (Throwable) Class.forName(exception).newInstance();
            } else {
                t = (Throwable) Class.forName(exception).getConstructor(String.class).newInstance(messageException);
            }

            logger.error(message, t);
        }
    }

    @Override
    String title() {
        return " Log4J";
    }
}
