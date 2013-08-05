package org.skarb.vertx.mods.test.integration;

import org.junit.Test;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import static org.vertx.testtools.VertxAssert.assertEquals;
import static org.vertx.testtools.VertxAssert.testComplete;

/**
 * Test view access.
 * User: skarb
 * Date: 30/07/13
 */
public class DoSetTest extends CouchBasePersistorLauncher {


    @Test
    public void test() {
        final JsonObject data = new JsonObject();
        data.putString("action", "set").putObject("document", new JsonObject().putString("name", "toto"));
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                final JsonObject body = assertError(message);
                assertEquals(1L, body.getNumber("count").longValue());
                testComplete();


            }
        });
    }


}
