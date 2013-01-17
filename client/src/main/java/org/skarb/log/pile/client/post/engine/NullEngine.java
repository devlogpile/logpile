package org.skarb.log.pile.client.post.engine;

import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.util.LogpileException;

/**
 * NUll implementation.
 * User: skarb
 * Date: 17/01/13
 */
public class NullEngine implements Engine {

    public NullEngine() {
        System.err.println("Logpile : No engine configured. Null implementation used.");
    }

    public void post(Event event) throws LogpileException {

    }
}
