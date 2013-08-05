package org.skarb.vertx.mods.test.integration;

import org.junit.Test;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Test view access.
 * User: skarb
 * Date: 30/07/13
 */
public class DoViewTest extends CouchBasePersistorLauncher {

    @Test
    public void testDoView() {
        final JsonObject data = new JsonObject();
        data.putString("action", "view").putString("documentName", "beer").putString("view", "brewery_beers");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                final JsonObject body = assertError(message);
                final Number total_rows = body.getNumber("total_rows");
                assertNotNull(total_rows);
                assertTrue(total_rows.intValue() > 0);
                final JsonArray rows = body.getArray("rows");
                assertNotNull(rows);
                assertTrue(rows.size() > 0);
                testComplete();
            }
        });
    }

    @Test
    public void testNoView() {
        final JsonObject data = new JsonObject();
        data.putString("action", "view").putString("documentName", "beer");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                assertEquals("error", message.body().getString("status"));
                testComplete();
            }
        });
    }

    @Test
    public void testNodocumentName() {
        final JsonObject data = new JsonObject();
        data.putString("action", "view").putString("view", "brewery_beers");
        getVertx().eventBus().send(CouchBasePersistor.DEFAULT_ADDRESS, data, new Handler<Message<JsonObject>>() {
            public void handle(final Message<JsonObject> message) {
                assertEquals("error", message.body().getString("status"));
                testComplete();
            }
        });
    }
}
