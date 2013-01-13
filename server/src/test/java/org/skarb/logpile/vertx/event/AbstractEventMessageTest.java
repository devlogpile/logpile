package org.skarb.logpile.vertx.event;

import org.junit.Test;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 06/01/13
 */
@SuppressWarnings("unchecked ")
public class AbstractEventMessageTest {

    @Test
    public void testhandlet() throws Exception {
        final Mock mock = new Mock();


        final Message message = mock(Message.class);
        message.body = new JsonObject().putString("application", "test");
        mock.handle(message);
        assertTrue(mock.call);
        verify(message).reply(anyObject());
    }

    private static class Mock extends AbstractEventMessage {
        boolean call = false;

        @Override
        public boolean handle(Event event) {
            call = true;
            return false;
        }

        @Override
        public String describe() {
            return "";
        }
    }
}
