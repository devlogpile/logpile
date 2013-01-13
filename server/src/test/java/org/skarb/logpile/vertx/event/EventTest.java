package org.skarb.logpile.vertx.event;


import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;


/**
 * User: skarb
 * Date: 03/01/13
 */
public class EventTest {

    @Test
    public void testGetStack() {
        final JsonObject tmp = new JsonObject();
        final Exception ex  =new Exception("ssss message");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(out));


        tmp.putString(Event.PROP_STACK, new String(out.toByteArray()));
        Event event = Event.Builder(tmp);
        assertTrue( event.getStackTrace().contains("ssss message"));
    }



    @Test
    public void testGetApplication() {
        final HashMap<String, Object> tmp = new HashMap<>();
        tmp.put(Event.PROP_APPLICATION, "1111");
        Event event = Event.Builder(tmp);
        assertEquals("1111", event.getApplication());
    }

    @Test
    public void testgetComponent() {
        final HashMap<String, Object> tmp = new HashMap<>();
        tmp.put(Event.PROP_COMPONENT, "sss");
        Event event = Event.Builder(tmp);
        assertEquals("sss", event.getComponent());
    }

    @Test
    public void testgetMessage() {
        final HashMap<String, Object> tmp = new HashMap<>();
        tmp.put(Event.PROP_MESSAGE, "message");
        Event event = Event.Builder(tmp);
        assertEquals("message", event.getMessage());
    }

    @Test
    public void testgetTime() {
        final HashMap<String, Object> tmp = new HashMap<>();
        Event event0 = Event.Builder(tmp);
        assertNull(event0.getTime());
        long value = System.currentTimeMillis();
        tmp.put(Event.PROP_DATE, "" + value);
        Event event = Event.Builder(tmp);
        assertEquals(Long.valueOf(value), event.getTime());
    }

    @Test
    public void testgetDate() {
        final HashMap<String, Object> tmp = new HashMap<>();
        Event event0 = Event.Builder(tmp);
        assertNull(event0.toDate());
        long value = System.currentTimeMillis();
        tmp.put(Event.PROP_DATE, "" + value);
        Event event = Event.Builder(tmp);
        assertEquals(new Date(value), event.toDate());
    }
}
