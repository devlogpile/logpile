package org.skarb.logpile.vertx.event;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class ConsoleOutEvent extends AbstractConsoleEvent {


    public ConsoleOutEvent() {
        super(System.out);
    }

    @Override
    public String describe() {
        return "Console out the error event";
    }
}
