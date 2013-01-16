package org.skarb.log.pile.client.post.engine.rest;

/**
 *  Call the web service with an Get Method.
 */
public class EngineRestGet extends AbstractEngineRest {


    /**
     * Visible for Test
     */
    EngineRestGet(HttpConnector connector) {
        super(connector, HttpConnector.Method.GET);
    }

    /**
     * Default Constructor.
     */
    public EngineRestGet() {
        this(new HttpConnector());
    }

}
