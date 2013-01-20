package org.skarb.log.pile.client.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: skarb
 * Date: 20/01/13
 */
public class LogbackError extends Error {

    private static final Logger logger = LoggerFactory.getLogger(LogbackError.class);

    @Override
    void dolog(String mess) throws Exception {
        logger.error(mess);
    }

    @Override
    void dolog(String mess, Throwable throwable) throws Exception {
        logger.error(mess, throwable);
    }

    @Override
    String title() {
        return " Logback";
    }
}
