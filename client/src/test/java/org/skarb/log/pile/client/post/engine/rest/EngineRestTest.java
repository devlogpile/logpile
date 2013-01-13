package org.skarb.log.pile.client.post.engine.rest;

import org.junit.Test;
import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.util.ClientConstantes;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test of the class EngineRest.
 */
public class EngineRestTest {

    @Test
    public void testGetUrl() throws Exception {
        System.setProperty(ClientConstantes.PROPERTIES_URL_REST, "http://www.gogole.fr");
        EngineRest engineRest = new EngineRest();
        assertEquals("http://www.gogole.fr", engineRest.getUrl());
        assertEquals("http://www.gogole.fr", engineRest.getUrl());
        engineRest.reset();
        assertEquals("http://www.gogole.fr", engineRest.getUrl());
        System.setProperty(ClientConstantes.PROPERTIES_URL_REST, "http://www.toto.fr");
        engineRest.reset();
        assertEquals("http://www.toto.fr", engineRest.getUrl());


    }


    @Test
    public void testencodeString() throws Exception {
        EngineRest engineRest = new EngineRest();
        assertEquals("", engineRest.encode((String) null));
        assertEquals("", engineRest.encode(""));

        assertEquals("sss", engineRest.encode("sss"));
        assertEquals("ss+sss", engineRest.encode("ss sss"));
    }


    @Test
    public void testencodeDate() throws Exception {
        EngineRest engineRest = new EngineRest();
        assertEquals("", engineRest.encode((Date) null));
        Date value = new Date();
        assertEquals("" + value.getTime(), engineRest.encode(value));

    }

    @Test
    public void testpost() throws Exception {
        final HttpConnector mock = mock(HttpConnector.class);
        final EngineRest engineRest = new EngineRest(mock);
        Event event = new Event();
        event.setApplication("app1");
        event.setComponent("compo");
        event.setMessage("date");
        event.setDate(new Date());
        engineRest.post(event);
    }
}
