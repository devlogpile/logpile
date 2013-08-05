package org.skarb.vertx.mods.test.integration;

import org.junit.Test;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import static org.vertx.testtools.VertxAssert.assertEquals;
import static org.vertx.testtools.VertxAssert.assertNotNull;
import static org.vertx.testtools.VertxAssert.testComplete;

/**
 * Test view access.
 * User: skarb
 * Date: 30/07/13
 */
public class DoAddTest extends CouchBasePersistorLauncher {


    @Test
    public void test() {
        final JsonObject data = new JsonObject().putString("action", "add").putObject("document", new JsonObject().putString("name", "toto"));
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                final JsonObject body = assertError(message);
                final String key = body.getString("key");
                assertEquals(1L, body.getNumber("count").longValue());
                assertNotNull(key);
                     data.putString("key",key);
                getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {

                    @Override
                    public void handle(Message<JsonObject> event) {
                        final JsonObject body1 = event.body();
                        assertEquals(0L, body1.getNumber("count").longValue());


                        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, new JsonObject().putString("key",key).putString("action", "replace").putObject("document", new JsonObject().putString("name", "tot4")), new Handler<Message<JsonObject>>() {

                            @Override
                            public void handle(Message<JsonObject> event2) {
                                final JsonObject body2 = assertError(event2);

                                assertEquals(1L, body2.getNumber("count").longValue());
                                testComplete();
                            }
                        });
                    }
                });

            }
        });
    }


}
