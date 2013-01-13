package org.skarb.log.pile.client.post.engine;

import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.util.LogpileException;

/**
 * Interface for the client implementation.
 *
 * @author trashy
 */
public interface Engine {
    /**
     * the property for "application".
     */
    public static final String PROP_APPLICATION = "application";
    /**
     * the property for component.
     */
    public static final String PROP_COMPONENT = "component";
    /**
     * the property for the error message.
     */
    public static final String PROP_MESSAGE = "message";
    /**
     * the property for the "exception".
     */
    public static final String PROP_STACK = "stacktrace";
    /**
     * the property for the "date of the error".
     */
    public static final String PROP_DATE = "date";

    /**
     * send the error to logpile server.
     *
     * @param event the event
     * @throws Exception
     */
    public void post(final Event event) throws LogpileException;
}
