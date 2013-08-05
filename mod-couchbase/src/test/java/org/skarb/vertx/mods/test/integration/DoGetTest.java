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
public class DoGetTest extends CouchBasePersistorLauncher {

    @Test
    public void testDo() {
        final JsonObject data = new JsonObject();
        data.putString("action", "get").putString("key", "21st_amendment_brewery_cafe");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                final JsonObject body = assertError(message);
                assertEquals( 1L, body.getNumber("count").longValue());
                final JsonObject document = body.getObject("document");
                assertNotNull(document);
                assertEquals("21st Amendment Brewery Cafe",document.getString("name"));

                testComplete();
            }
        });
    }

    @Test
    public void testUnknown() {
        final JsonObject data = new JsonObject();
        data.putString("action", "get").putString("key", "azertydsqwddsdf");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                final JsonObject body = assertError(message);
                assertEquals( 0L, body.getNumber("count").longValue());

                testComplete();
            }
        });
    }



    @Test
    public void testNoKey() {
        final JsonObject data = new JsonObject();
        data.putString("action", "get");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                assertEquals("error", message.body().getString("status"));
                testComplete();
            }
        });
    }
}
