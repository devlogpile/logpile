package org.skarb.logpile.vertx.handler;


import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.logpile.vertx.EventManager;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.platform.Container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 03/01/13
 */
@SuppressWarnings("unchecked ")
public class PostParamsTest {

    @Test
    public void testEmpty() {
        final EventManager eventManager = mock(EventManager.class);
        final PostParams postParams = new PostParams(eventManager, mock(Container.class));
        postParams.handle(new Buffer());

        final ArgumentCaptor<MultiMap> arguments = ArgumentCaptor.forClass(MultiMap.class);
        verify(eventManager, only()).run(arguments.capture());
        assertTrue(arguments.getValue().isEmpty());
    }

    @Test
    public void testhandle() {
        final EventManager eventManager = mock(EventManager.class);
        final PostParams postParams = new PostParams(eventManager, mock(Container.class));
        final Buffer buff = new Buffer();
        buff.appendString("param1=value1&param2=value2");
        postParams.handle(buff);

        final ArgumentCaptor<MultiMap> arguments = ArgumentCaptor.forClass(MultiMap.class);
        verify(eventManager, only()).run(arguments.capture());
        assertEquals(2, arguments.getValue().entries().size());
        assertEquals("value1", arguments.getValue().get("param1"));
        assertEquals("value2", arguments.getValue().get("param2"));
    }

    @Test
    public void testhandleOne() {
        final EventManager eventManager = mock(EventManager.class);
        final PostParams postParams = new PostParams(eventManager, mock(Container.class));
        final Buffer buff = new Buffer();
        buff.appendString("param1=value1");
        postParams.handle(buff);

        final ArgumentCaptor<MultiMap> arguments = ArgumentCaptor.forClass(MultiMap.class);
        verify(eventManager, only()).run(arguments.capture());
        assertEquals(1, arguments.getValue().entries().size());
        assertEquals("value1", arguments.getValue().get("param1"));

    }
}
