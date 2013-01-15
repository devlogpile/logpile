package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.Formatter;
import org.vertx.java.core.Vertx;
import org.vertx.java.deploy.Container;

import java.io.PrintStream;

/**
 * Abstract class for console logging of the event.
 * User: skarb
 * Date: 06/01/13
 */

abstract class AbstractConsoleEvent extends AbstractEventMessage {
    /**
     * the type of the stream.
     */
    private final PrintStream printStream;
    /**
     * the formatter.
     */
    private Formatter formatter;

    /**
     * The constructor.
     * @param printStream the type of stream.
     */
    AbstractConsoleEvent(final PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * init the default formatter.
     * @param vertx    the current instance.
     * @param container the current instance.
     */
    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);
        formatter = Formatter.Builder.init().defaultValues().build();
    }

    /**
     * print the event.
     * @param event the event
     * @return always true.
     */
    @Override
    public boolean handle(final Event event) {
        final String line = formatter.format(event);
        printStream.println(line);
        return true;
    }

    /**
     * use for testing.
     * @return the current value.
     */
    PrintStream getPrintStream() {
        return printStream;
    }
}
