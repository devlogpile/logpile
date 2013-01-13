package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.Formatter;
import org.vertx.java.core.Vertx;

import java.io.PrintStream;

/**
 * User: skarb
 * Date: 06/01/13
 */

abstract class AbstractConsoleEvent extends AbstractEventMessage {
    private final PrintStream printStream;
    private Formatter formatter;

    /**
     * @param printStream
     */
    AbstractConsoleEvent(final PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void setVertx(Vertx vertx) {
        super.setVertx(vertx);
        formatter = Formatter.Builder.init().defaultValues().build();
    }

    @Override
    public boolean handle(final Event event) {
        final String line = formatter.format(event);
        printStream.println(line);
        return true;
    }

    PrintStream getPrintStream() {
        return printStream;
    }
}
