package org.skarb.logpile.vertx.event;

/**
 * print the event on out console.
 * User: skarb
 * Date: 03/01/13
 */
public class ConsoleOutEvent extends AbstractConsoleEvent {

    /**
     * Constructor.
     */
    public ConsoleOutEvent() {
        super(System.out);
    }

    @Override
    public String describe() {
        return "Console out the error event";
    }
}
