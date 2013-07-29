package org.skarb.logpile.vertx;


import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.HttpVersion;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Container;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.cert.X509Certificate;
import java.net.InetSocketAddress;
import java.net.URI;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 03/01/13
 */
public class EventJsonVerticleTest {

    @Test
    public void testreturnResponse() {
        final HttpServerResponse httpServerResponse = mock(HttpServerResponse.class);
        //final HttpServerRequest httpServerRequest = new HttpServerRequest(, "", "", "", httpServerResponse) {};
        final HttpServerRequest httpServerRequest = new HttpServerRequest() {
            @Override
            public HttpVersion version() {
                return null;  
            }

            @Override
            public String method() {
                return "GET";
            }

            @Override
            public String uri() {
                return "";
            }

            @Override
            public String path() {
                return "";
            }

            @Override
            public String query() {
                return "";
            }

            @Override
            public HttpServerResponse response() {
                return httpServerResponse;
            }

            @Override
            public MultiMap headers() {
                return null;
            }

            @Override
            public MultiMap params() {
                return null;  
            }

            @Override
            public InetSocketAddress remoteAddress() {
                return null;  
            }

            @Override
            public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
                return new X509Certificate[0];  
            }

            @Override
            public URI absoluteURI() {
                return null;  
            }

            @Override
            public HttpServerRequest bodyHandler(Handler<Buffer> bodyHandler) {
                return null;  
            }

            @Override
            public NetSocket netSocket() {
                return null;  
            }

            @Override
            public HttpServerRequest expectMultiPart(boolean expect) {
                return null;  
            }

            @Override
            public HttpServerRequest uploadHandler(Handler<HttpServerFileUpload> uploadHandler) {
                return null;  
            }

            @Override
            public MultiMap formAttributes() {
                return null;  
            }

            @Override
            public HttpServerRequest dataHandler(Handler<Buffer> handler) {
                return null;  
            }

            @Override
            public HttpServerRequest pause() {
                return null;  
            }

            @Override
            public HttpServerRequest resume() {
                return null;  
            }

            @Override
            public HttpServerRequest endHandler(Handler<Void> endHandler) {
                return null;  
            }

            @Override
            public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
                return null;  
            }
        }   ;
        EventJsonVerticle.returnResponse(httpServerRequest);
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(httpServerRequest.response()).end(argument.capture());
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
