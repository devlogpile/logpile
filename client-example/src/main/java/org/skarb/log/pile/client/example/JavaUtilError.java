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

    private static final Logger logger = Logger.getLogger(SwingMain.class.getName());

    public JavaUtilError() {
        super();
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
    void dolog(String mess) throws Exception {
        logger.severe(mess);
    }

    @Override
    void dolog(String mess, Throwable throwable) throws Exception {
        logger.log(Level.SEVERE, mess, throwable);
    }

    @Override
    String title() {
        return " Java Util Logging";
    }
}
