package org.skarb.logpile.vertx.event;

/**
 * Print the error event in console.
 * User: skarb
 * Date: 03/01/13
 */
public class ConsoleErrEvent extends AbstractConsoleEvent {

    /**
     * Constructor.
     */
    public ConsoleErrEvent() {
        super(System.err);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public String describe() {
        return "Console error the event";
    }
}
