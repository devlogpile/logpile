package org.skarb.logpile.vertx.web;

import org.junit.Test;
import org.skarb.logpile.vertx.MainVerticle;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;

import static org.mockito.Mockito.*;
/**
 * User: skarb
 * Date: 10/01/13
 */
@SuppressWarnings("unchecked ")
public class LogpileWebTest {

    public class MockLogPileWeb extends LogpileWeb {

        Vertx vertx;
        Container container;

        public MockLogPileWeb(){
            super();
            vertx = mock(Vertx.class);
            final EventBus eventbus = mock(EventBus.class);

            when(vertx.eventBus()).thenReturn(eventbus);

            container = mock(Container.class);
            final JsonObject returnValue = new JsonObject();
            JsonObject value = new JsonObject();
            value.putNumber(MainVerticle.INSTANCE_FIELD,1);
            returnValue.putObject(LogpileWeb.WEB_SERVER, value);
            when(container.getConfig()).thenReturn(returnValue);
        }

        @Override
        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public Container getContainer() {
            return container;
        }
    }



    @Test
    public void teststart() throws Exception {
        MockLogPileWeb logpileWeb = new MockLogPileWeb();
        logpileWeb.start();
        verify(logpileWeb.vertx.eventBus()).registerHandler(LogpileWeb.SERVER_STATUS,logpileWeb);
    }


    @Test
    public void testhandle() throws Exception {
        final MockLogPileWeb logpileWeb = new MockLogPileWeb();
        final Message message = mock(Message.class);
        logpileWeb.handle(message);

    }
}
