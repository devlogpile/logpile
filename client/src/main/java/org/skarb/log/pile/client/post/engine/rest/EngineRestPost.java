package org.skarb.log.pile.client.post.engine.rest;

/**
 * Call the web service with an Post Method.
 */
public class EngineRestPost extends AbstractEngineRest {

    /**
     * Visible for Test
     */
    EngineRestPost(HttpConnector connector) {
        super(connector, HttpConnector.Method.POST);
    }

    /**
     * Default Constructor.
     */
    public EngineRestPost() {
        this(new HttpConnector());
    }

}
