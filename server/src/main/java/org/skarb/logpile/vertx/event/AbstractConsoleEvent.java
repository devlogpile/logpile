package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.event.format.LineFormatter;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.Container;

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
     * the lineFormatter.
     */
    private LineFormatter lineFormatter;

    /**
     * The constructor.
     * @param printStream the type of stream.
     */
    AbstractConsoleEvent(final PrintStream printStream) {
        this.printStream = printStream;
    }

    /**
     * init the default lineFormatter.
     * @param vertx    the current instance.
     * @param container the current instance.
     */
    @Override
    public void setDatas(final Vertx vertx, final Container container) {
        super.setDatas(vertx, container);
        lineFormatter = LineFormatter.Builder.init().defaultValues().build();
    }

    /**
     * print the event.
     * @param event the event
     * @return always true.
     */
    @Override
    public boolean handle(final Event event) {
        final String line = lineFormatter.format(event);
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
