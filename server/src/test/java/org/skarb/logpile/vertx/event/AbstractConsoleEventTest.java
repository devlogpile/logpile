package org.skarb.logpile.vertx.event;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 10/01/13
 */
public class AbstractConsoleEventTest {

    @Test
    public void testHandleEmpty() {
        final Mock mock = new Mock();
        mock.setDatas(mock(Vertx.class), mock(Container.class));
        mock.handle(Event.Builder(new JsonObject()));
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(mock.getPrintStream(), atLeastOnce()).println(argument.capture());
        assertFalse(argument.getValue().isEmpty());
    }

    @Test
    public void testHandleNotEmpty() {
        final Mock mock = new Mock();

        mock.setDatas(mock(Vertx.class), mock(Container.class));
        Date date = new Date();
        final Exception ex = new Exception("except");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(out));

        mock.handle(Event.Builder(new JsonObject().putString(Event.PROP_APPLICATION, "app")
                .putString(Event.PROP_COMPONENT, "compo").putString(Event.PROP_DATE, "" + date.getTime())
                .putString(Event.PROP_MESSAGE, "mess").putString(Event.PROP_STACK, new String(out.toByteArray()))));
        final ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(mock.getPrintStream(), atLeastOnce()).println(argument.capture());
        final String message = argument.getValue();
        assertTrue(message.contains("app"));
        assertTrue(message.contains("compo"));
        assertTrue(message.contains("mess"));
        assertTrue(message.contains("except"));
    }

    class Mock extends AbstractConsoleEvent {

        public Mock() {
            super(mock(PrintStream.class));
        }

        @Override
        public String describe() {
            return "mock";
        }
    }
}
