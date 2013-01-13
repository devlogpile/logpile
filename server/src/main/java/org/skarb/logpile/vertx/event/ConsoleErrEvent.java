package org.skarb.logpile.vertx.event;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class ConsoleErrEvent extends AbstractConsoleEvent {


    public ConsoleErrEvent() {
        super(System.err);
    }

    @Override
    public String describe() {
        return "Console error the event";
    }
}
