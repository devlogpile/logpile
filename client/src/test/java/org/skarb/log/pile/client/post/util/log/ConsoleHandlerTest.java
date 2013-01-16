package org.skarb.log.pile.client.post.util.log;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test of {@link ConsoleHandler}.
 * User: skarb
 * Date: 16/01/13
 */
public class ConsoleHandlerTest {

    @Test
    public void test() throws Exception {
        final Logger logger = Logger.getLogger(JavaUtilLogHandlerTest.class.getName());
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();
        instance.setApplication("application");
        Engine engine = mock(Engine.class);

        instance.setEngine(engine);

        final ConsoleHandler handler = new ConsoleHandler();

        logger.addHandler(handler);

        logger.setLevel(Level.SEVERE);
        // TEST avec exception
        logger.log(Level.SEVERE, "ddd", new Exception("truc"));
        final ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(engine).post(argument.capture());
        assertEquals("org.skarb.log.pile.client.post.util.log.ConsoleHandlerTest test", argument.getValue().getComponent());
        assertEquals("application", argument.getValue().getApplication());
        assertEquals("ddd", argument.getValue().getMessage());
        assertTrue(argument.getValue().getStacktrace().contains("truc"));
        assertNotNull(argument.getValue().getDate());

        reset(engine);

        // Not send event
        logger.fine("essai");

        verifyZeroInteractions(engine);

        logger.removeHandler(handler);
    }

    /**
     * Test catch Exception
     *
     * @throws Exception
     */
    @Test
    public void testException() throws Exception {

        final Logger logger = Logger.getLogger(JavaUtilLogHandlerTest.class.getName());
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();
        instance.setApplication("application");
        Engine engine = mock(Engine.class);

        instance.setEngine(engine);

        ConsoleHandler handler = new ConsoleHandler();
        logger.addHandler(handler);

        logger.setLevel(Level.SEVERE);
        doThrow(Exception.class).when(engine).post((Event) anyObject());

        logger.fine("dsdd");
        verify(engine, never()).post((Event) anyObject());

        logger.severe("dsdd");
        verify(engine, only()).post((Event) anyObject());
        logger.removeHandler(handler);
    }
}
