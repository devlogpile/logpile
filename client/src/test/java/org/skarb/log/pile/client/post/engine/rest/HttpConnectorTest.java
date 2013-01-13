package org.skarb.log.pile.client.post.engine.rest;

import org.junit.Test;
import org.skarb.log.pile.client.util.LogpileException;

import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: skarb
 * Date: 30/12/12
 */
public class HttpConnectorTest {

    @Test
    public void testcreateParameters() throws Exception {
        HttpConnector httpConnector = new HttpConnector();

        assertEquals("", httpConnector.createParameters(new HashMap<String, String>()));

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("p1", "v1");

        assertEquals("?p1=v1", httpConnector.createParameters(parameters));
        parameters.put("pa", "v2");

        assertEquals("?pa=v2&p1=v1", httpConnector.createParameters(parameters));
    }

    @Test
    public void testurlConnection() throws Exception {
        final HttpConnector httpConnector = new HttpConnector();
        HttpURLConnection httpURLConnection = httpConnector.urlConnection("http://url.fr", HttpConnector.Method.POST, "");
        assertNotNull(httpURLConnection);
        HttpURLConnection httpURLConnection2 = httpConnector.urlConnection("http://url.fr", HttpConnector.Method.GET, "");
        assertNotNull(httpURLConnection2);
    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseThrowsException() throws Exception {
        final HttpConnector httpConnector = new HttpConnector();
        final HttpURLConnection mock = mock(HttpURLConnection.class);
        when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        when(mock.getInputStream()).thenThrow(Exception.class);
        httpConnector.treatResponse(mock);

    }

    @Test
    public void testtreatResponse() throws Exception {
        final HttpConnector httpConnector = new HttpConnector();
        final HttpURLConnection mock = mock(HttpURLConnection.class);
        when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(HttpConnector.RESULT_OK.getBytes());
        when(mock.getInputStream()).thenReturn(byteArrayInputStream);
        httpConnector.treatResponse(mock);

    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseWithoutResult() throws Exception {

            final HttpConnector httpConnector = new HttpConnector();
            final HttpURLConnection mock = mock(HttpURLConnection.class);
            when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);


            httpConnector.treatResponse(mock);



    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseBadResult() throws Exception {
        try {
            final HttpConnector httpConnector = new HttpConnector();
            final HttpURLConnection mock = mock(HttpURLConnection.class);
            when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("bad result".getBytes());
            when(mock.getInputStream()).thenReturn(byteArrayInputStream);
            httpConnector.treatResponse(mock);


        } catch (Exception e) {
            assertTrue(e.getMessage().contains("bad result"));
            throw e;
        }
    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseUnknownCode() throws Exception {
        final HttpConnector httpConnector = new HttpConnector();
        final HttpURLConnection mock = mock(HttpURLConnection.class);
        when(mock.getResponseCode()).thenReturn(-1);
        httpConnector.treatResponse(mock);

    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseBadrequest() throws Exception {
        final HttpConnector httpConnector = new HttpConnector();
        final HttpURLConnection mock = mock(HttpURLConnection.class);
        when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        httpConnector.treatResponse(mock);

    }

    @Test(expected = LogpileException.class)
    public void testtreatResponseBadmethod() throws Exception {
        try {
            final HttpConnector httpConnector = new HttpConnector();
            final HttpURLConnection mock = mock(HttpURLConnection.class);
            when(mock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_INTERNAL_ERROR);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("internal error".getBytes());
            when(mock.getErrorStream()).thenReturn(byteArrayInputStream);
            httpConnector.treatResponse(mock);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("internal error"));
            throw e;
        }

    }

}
