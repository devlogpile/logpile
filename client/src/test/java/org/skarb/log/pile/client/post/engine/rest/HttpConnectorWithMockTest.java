package org.skarb.log.pile.client.post.engine.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Test;
import org.skarb.log.pile.client.util.LogpileException;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 11/01/13
 */
public class HttpConnectorWithMockTest {

    public class Mock extends HttpConnector {

        public HttpURLConnection httpURLConnection;


        public Mock() {
            super();
            httpURLConnection =  mock(HttpURLConnection.class);
        }

        @Override
        HttpURLConnection urlConnection(String url, Method method, String params) throws Exception {
            return httpURLConnection;
        }

        @Override
        void treatResponse(HttpURLConnection urlConnection) throws LogpileException {

        }
    }

    @Test
    public void testSend() throws Exception {
        final Mock mock = new Mock();
        mock.send("url", HttpConnector.Method.GET,new HashMap<String, String>());
        verify(mock.httpURLConnection,atLeastOnce()).connect();
    }

    @Test(expected = LogpileException.class)
    public void testSendException() throws Exception {
        final Mock mock = new Mock();
        doThrow(IOException.class).when(mock.httpURLConnection).connect();
        mock.send("url", HttpConnector.Method.GET,new HashMap<String, String>());

    }

    @Test(expected = LogpileException.class)
    public void testSendLogpileException() throws Exception {
        final Mock mock = new Mock();
        doThrow(LogpileException.class).when(mock.httpURLConnection).connect();
        mock.send("url", HttpConnector.Method.GET,new HashMap<String, String>());

    }
}
