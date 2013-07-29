package org.skarb.logpile.vertx;

import org.junit.Test;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;


import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 02/01/13
 */
public class MainVerticleTest {

    @Test
    public void testStart() throws Exception {

        final MockMainVerticle mockMainVerticle = new MockMainVerticle();

        mockMainVerticle.start();

    }

    @Test
    public void testStartActiveTrue() throws Exception {

        final MockMainVerticle mockMainVerticle = new MockMainVerticle();
        mockMainVerticle.configlogPile.putBoolean(MainVerticle.ACTIVE_FIELD,true);
        mockMainVerticle.start();

    }

    private static class MockMainVerticle extends MainVerticle {


        private final Container monckcontainer;
        private final Logger logger;
        private final Vertx vertx;
        final JsonObject config;
        final JsonObject configlogPile;

        private MockMainVerticle() {
            monckcontainer = mock(Container.class);
            logger = mock(Logger.class);
            when(monckcontainer.logger()).thenReturn(logger);
            config = new JsonObject();
            final JsonObject event = new JsonObject();
            config.putObject(MainVerticle.EVENT_JSON, event);
            configlogPile = new JsonObject();
            config.putObject(MainVerticle.LOG_PILE_WEB, configlogPile);
            event.putNumber(MainVerticle.INSTANCE_FIELD,1);
            when(monckcontainer.config()).thenReturn(config);
            vertx = mock(Vertx.class);
            EventBus mock = mock(EventBus.class);
            when(vertx.eventBus()).thenReturn(mock);
        }

        public Container getContainer() {
            return monckcontainer;
        }

        public Vertx getVertx(){
            return vertx;
        }
    }
}
