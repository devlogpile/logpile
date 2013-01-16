package org.skarb.log.pile.client.post.util.log;

import org.junit.Test;
import org.skarb.log.pile.client.Event;
import org.skarb.log.pile.client.post.engine.Engine;
import org.skarb.log.pile.client.util.JavaUtilLogData;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

/**
 * Test Exception management.
 */
public class JavaUtilLogHandlerExceptionTest {

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

        JavaUtilLogHandler handler = new JavaUtilLogHandler();
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
