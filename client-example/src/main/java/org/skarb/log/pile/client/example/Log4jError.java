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



    @Override
    protected void init() {
        URL resource = Thread.currentThread().getClass().getResource("/log4j.properties");
        PropertyConfigurator.configure(resource.getPath());
    }

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
        return " Log4J";
    }
}
