package org.skarb.log.pile.client.post.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
/**
 * User: skarb
 * Date: 11/01/13
 */
public class LogbackAppenderTest {

    private static Logger logger;
    private static Engine eng;

    @BeforeClass
    public static void init(){
        logger = (Logger) LoggerFactory.getLogger(LogbackAppenderTest.class);
        logger.setLevel(Level.ERROR);
        eng = mock(Engine.class);
        final LogbackAppender appender = new LogbackAppender(eng, "app1");
        appender.start();
        logger.addAppender(appender);
    }

    @Test
    public void testLogLevel() throws Exception{

        reset(eng);

        logger.debug("TEST debug");
        verify(eng,never()).post((Event) any());
    }

    @Test
    public void testLog() throws Exception {

        reset(eng);

        logger.error("TEST Error");

        final ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(eng, atLeastOnce()).post(argument.capture());
        assertEquals("app1",argument.getValue().getApplication());
        assertEquals(LogbackAppenderTest.class.getName(),argument.getValue().getComponent());
        assertEquals("TEST Error",argument.getValue().getMessage());
        assertTrue(argument.getValue().getDate().getTime() > 0L);
        assertNull(argument.getValue().getStacktrace());
    }

    @Test
    public void testLogWithStackTrace() throws Exception {
        reset(eng);
        logger.error("TEST Exception",new Exception("one error"));
        final ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(eng, atLeastOnce()).post(argument.capture());
        assertEquals("app1",argument.getValue().getApplication());
        assertEquals(LogbackAppenderTest.class.getName(),argument.getValue().getComponent());
        assertEquals("TEST Exception",argument.getValue().getMessage());
        assertTrue(argument.getValue().getDate().getTime() > 0L);
        assertTrue(argument.getValue().getStacktrace().contains("one error"));
    }

    @Test
    public void testLogThrowsException() throws Exception {
        reset(eng);
        doThrow(Exception.class).when(eng).post(any(Event.class));
        logger.error("TEST Exception throws");
        verify(eng, atLeastOnce()).post(any(Event.class));

    }

    @Test
    public void testLogThrowsRuntimeException() throws Exception {
        reset(eng);
        doThrow(RuntimeException.class).when(eng).post(any(Event.class));
        logger.error("TEST Runtime");
        verify(eng, atLeastOnce()).post(any(Event.class));
    }

    @Test
    public void testLogStart() throws Exception {
        final LogbackAppender appender = new LogbackAppender();
        appender.start();
        assertNotNull(appender.getEngine());
        assertFalse(appender.getApplication().isEmpty());
    }


}
