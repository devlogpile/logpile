package org.skarb.logpile.vertx;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;

import java.util.Map;

/**
 * Interface for the manager which manage the receivers of the events.
 * User: skarb
 * Date: 03/01/13
 */
public interface EventManager {

    /**
     * The name of the service for getting the service to treat the events.
     */
    public static final String SERVICE_STATE = "logpile-getState";
    /**
     * initialize the event manager.
     * @param vertx for the acessing in event bus.
     * @param container the configuration.
     */
    void init(final Vertx vertx, final Container container);

    /**
     * send the error events.
     *
     * @param params parameters.
     */
    void run(final Map<String, String> params);
}
