package org.skarb.vertx.mods.test.integration;

import org.junit.Test;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Test view access.
 * User: skarb
 * Date: 30/07/13
 */
public class DoStoreTest extends CouchBasePersistorLauncher {


    @Test
    public void testNodDoc() {
        final JsonObject data = new JsonObject();
        data.putString("action", "set");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                assertEquals("error", message.body().getString("status"));
                testComplete();


            }
        });
    }




}
