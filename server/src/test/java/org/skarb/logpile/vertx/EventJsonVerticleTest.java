package org.skarb.logpile.vertx;


import static junit.framework.Assert.*;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.deploy.Container;

import java.util.Map;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class EventJsonVerticleTest {

    @Test
    public void testreturnResponse(){
        final HttpServerResponse httpServerResponse = mock(HttpServerResponse.class);
        final HttpServerRequest httpServerRequest = new HttpServerRequest("GET", "", "", "", httpServerResponse) {
            @Override
            public Map<String, String> headers() {
              return null;
            }

            @Override
            public Map<String, String> params() {
                return null;
            }

            @Override
            public void dataHandler(Handler<Buffer> handler) {

            }

            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void exceptionHandler(Handler<Exception> handler) {

            }

            @Override
            public void endHandler(Handler<Void> endHandler) {

            }
        };

        EventJsonVerticle.returnResponse(httpServerRequest);
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(httpServerRequest.response).end(argument.capture());
        assertTrue(argument.getValue().contains("true"));
    }

    @Test
    public void testStart() throws Exception {
        final MockEventJsonVerticle mockEventJsonVerticle = new MockEventJsonVerticle();
        mockEventJsonVerticle.start();
    }

    private class MockEventJsonVerticle extends EventJsonVerticle {

        public Vertx getVertx() {
            return mock(Vertx.class, RETURNS_DEEP_STUBS);
        }

        public Container getContainer() {
            return mock(Container.class, RETURNS_DEEP_STUBS);
        }

    }
}
