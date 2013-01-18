package org.skarb.log.pile.client.example;

import javax.swing.*;
import java.util.logging.Logger;

/**
 *
 * User: skarb
 * Date: 16/01/13
 */
public class SwingTest {


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){

                final Window window = new Window(TypeLogger.JAVA_LOGGING);
                window.setVisible(true);//On la rend visible
            }
        });


    }
}
