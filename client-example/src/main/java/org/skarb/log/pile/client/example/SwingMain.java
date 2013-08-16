package org.skarb.log.pile.client.example;

import javax.swing.*;

/**
 * User: skarb
 * Date: 16/01/13
 */
public class SwingMain {


    public static final String NO_PARAMETERS = "Parameter value must be set : LOG4J,JAVA_LOGGING,LOGBACK";

    public static void main(final String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException(NO_PARAMETERS);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                String arg = args[0];
                if (arg == null || arg.trim().isEmpty()) {
                    throw new IllegalArgumentException(NO_PARAMETERS);
                }

                TypeLogger typeLogger = TypeLogger.valueOf(arg);

                final Window window = new Window(typeLogger);
                window.setVisible(true);//On la rend visible
            }
        });


    }
}
