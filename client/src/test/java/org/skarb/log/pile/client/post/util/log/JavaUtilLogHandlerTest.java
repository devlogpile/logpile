package org.skarb.log.pile.client.post.util.log;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JavaUtilLogHandlerTest {


    @Test
    public void testFormatStackTrace() {
        assertTrue(JavaUtilLogUtils.formatStackTrace(new Exception("TEst")).contains("java.lang.Exception: TEst"));
        assertEquals("", JavaUtilLogUtils.formatStackTrace(null));
    }

    @Test
    public void notConfigure() throws Exception {
        final Logger logger = Logger.getLogger(JavaUtilLogHandlerTest.class.getName());
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();

        Engine engine = mock(Engine.class);

        instance.setEngine(engine);

        final JavaUtilLogHandler handler = new JavaUtilLogHandler();
        logger.addHandler(handler);

        logger.severe("essai");
        verify(engine, only()).post((Event) anyObject());

        logger.removeHandler(handler);
    }

    @Test
    public void test() throws Exception {
        final Logger logger = Logger.getLogger(JavaUtilLogHandlerTest.class.getName());
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();
        instance.setApplication("application");
        Engine engine = mock(Engine.class);

        instance.setEngine(engine);

        final JavaUtilLogHandler handler = new JavaUtilLogHandler();
        logger.addHandler(handler);

        logger.setLevel(Level.SEVERE);
        // TEST avec exception
        logger.log(Level.SEVERE, "ddd", new Exception("truc"));
        final ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(engine).post(argument.capture());
        assertEquals("org.skarb.log.pile.client.post.util.log.JavaUtilLogHandlerTest test", argument.getValue().getComponent());
        assertEquals("application", argument.getValue().getApplication());
        assertEquals("ddd", argument.getValue().getMessage());
        assertTrue(argument.getValue().getStacktrace().contains("truc"));
        assertNotNull(argument.getValue().getDate());
        reset(engine);
        // TEST sans exception
        logger.severe("essai");
        final ArgumentCaptor<Event> argument2 = ArgumentCaptor.forClass(Event.class);
        verify(engine).post(argument2.capture());
        assertEquals("org.skarb.log.pile.client.post.util.log.JavaUtilLogHandlerTest test", argument2.getValue().getComponent());
        assertEquals("application", argument2.getValue().getApplication());
        assertEquals("essai", argument2.getValue().getMessage());
        assertTrue(argument2.getValue().getStacktrace().isEmpty());
        reset(engine);

        // Not send event
        logger.fine("essai");

        verifyZeroInteractions(engine);

        logger.removeHandler(handler);

    }

}
