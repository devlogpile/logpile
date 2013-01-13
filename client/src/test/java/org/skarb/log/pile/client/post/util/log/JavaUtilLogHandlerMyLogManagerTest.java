package org.skarb.log.pile.client.post.util.log;

import org.junit.Test;

import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: skarb
 * Date: 30/12/12
 */
public class JavaUtilLogHandlerMyLogManagerTest {


    @Test
    public void testgetLevelProperty() throws Exception {
        final LogManager mock = mock(LogManager.class);

        when(mock.getProperty("prop")).thenReturn(null);

        final JavaUtilLogHandler.MyLogManager myLogManager = new JavaUtilLogHandler.MyLogManager(mock);

        Level returnValue = myLogManager.getLevelProperty("prop", Level.FINE);
        assertEquals(Level.FINE, returnValue);

        when(mock.getProperty("prop")).thenReturn("SEVERE");

        Level returnValue2 = myLogManager.getLevelProperty("prop", Level.FINE);
        assertEquals(Level.SEVERE, returnValue2);

    }


    @Test
    public void testgetFilterProperty() throws Exception {
        final LogManager mock = mock(LogManager.class);

        when(mock.getProperty("prop")).thenReturn("org.skarb.log.pile.client.post.util.log.TestFilter");

        final JavaUtilLogHandler.MyLogManager myLogManager = new JavaUtilLogHandler.MyLogManager(mock);

        Filter returnValue = myLogManager.getFilterProperty("prop", null);
        assertTrue(returnValue instanceof TestFilter);


    }


    @Test
    public void testgetFormatterProperty() throws Exception {
        final LogManager mock = mock(LogManager.class);

        when(mock.getProperty("prop")).thenReturn("org.skarb.log.pile.client.post.util.log.TestFormatter");

        final JavaUtilLogHandler.MyLogManager myLogManager = new JavaUtilLogHandler.MyLogManager(mock);

        Formatter returnValue = myLogManager.getFormatterProperty("prop", null);
        assertTrue(returnValue instanceof TestFormatter);
    }


    @Test
    public void testgetStringProperty() throws Exception {
        final LogManager mock = mock(LogManager.class);

        when(mock.getProperty("prop")).thenReturn("ddd ");

        final JavaUtilLogHandler.MyLogManager myLogManager = new JavaUtilLogHandler.MyLogManager(mock);

        String returnValue = myLogManager.getStringProperty("prop", null);
        assertEquals("ddd", returnValue);
    }
}
