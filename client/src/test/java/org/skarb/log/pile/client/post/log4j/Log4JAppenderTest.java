package org.skarb.log.pile.client.post.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.log.pile.client.event.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Log4JAppenderTest {

    @Test
    public void logException() throws Exception {
        final Logger logger = Logger.getLogger(Log4JAppenderTest.class);
        logger.setLevel(Level.ERROR);
        Log4JAppender newAppender = new Log4JAppender();
        assertFalse(newAppender.requiresLayout());
        logger.addAppender(newAppender);
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();
        Engine engine = mock(Engine.class);
        instance.setEngine(engine);

        doThrow(Exception.class).when(engine).post((Event) anyObject());

        logger.log(Level.ERROR, "ddd", new Exception("truc"));

        verify(engine, only()).post((Event) anyObject());

        logger.removeAllAppenders();
    }

    @Test
    public void log() throws Exception {
        final Logger logger = Logger.getLogger(Log4JAppenderTest.class);
        logger.setLevel(Level.ERROR);
        logger.addAppender(new Log4JAppender());
        final JavaUtilLogData instance = JavaUtilLogData.getInstance();
        instance.setApplication("application");
        Engine engine = mock(Engine.class);


        instance.setEngine(engine);

        logger.log(Level.ERROR, "ddd", new Exception("truc"));
        ArgumentCaptor<Event> argument = ArgumentCaptor.forClass(Event.class);
        verify(engine).post(argument.capture());
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");


        assertEquals("application", argument.getValue().getApplication());
        assertEquals("ddd", argument.getValue().getMessage());
        assertEquals("org.skarb.log.pile.client.post.log4j.Log4JAppenderTest", argument.getValue().getComponent());
        assertTrue(argument.getValue().getStacktrace().contains("truc"));
        assertNotNull(argument.getValue().getDate());

        reset(engine);

        logger.error("essai");
        ArgumentCaptor<Event> argument2 = ArgumentCaptor.forClass(Event.class);
        verify(engine).post(argument2.capture());

        assertEquals("org.skarb.log.pile.client.post.log4j.Log4JAppenderTest", argument2.getValue().getComponent());
        assertEquals("application", argument2.getValue().getApplication());
        assertEquals("essai", argument2.getValue().getMessage());
        assertTrue(argument2.getValue().getStacktrace().isEmpty());
        reset(engine);


        logger.info("essai");

        verifyZeroInteractions(engine);

        logger.removeAllAppenders();
    }

}
