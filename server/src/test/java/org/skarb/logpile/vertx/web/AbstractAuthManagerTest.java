package org.skarb.logpile.vertx.web;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skarb.logpile.vertx.utils.MessageUtils;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: skarb
 * Date: 10/01/13
 */
@SuppressWarnings("unchecked ")
public class AbstractAuthManagerTest {

    @Test
    public void testDoLoginWhitoutUserName() {
        final MockAutjManager mock = new MockAutjManager(true);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject());
        mock.doLogin(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.ERROR_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoLoginWhitoutPassword() {
        final MockAutjManager mock = new MockAutjManager(true);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.USERNAME, "user"));

        mock.doLogin(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.ERROR_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoLoginKO() {
        final MockAutjManager mock = new MockAutjManager(false);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.USERNAME, "user")
                .putString(AbstractAuthManager.PASSWORD, "eee"));

        mock.loginHandler.handle(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.DENIED_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
        assertFalse(argument.getValue().getBoolean(MessageUtils.RESULT_FIELD));
    }

    @Test
    public void testDoLoginOK() {
        final MockAutjManager mock = new MockAutjManager(true);
        login(mock);
        login(mock);
    }

    private JsonObject login(MockAutjManager mock) {
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.USERNAME, "user")
                .putString(AbstractAuthManager.PASSWORD, "eee"));


        mock.doLogin(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());

        assertTrue(argument.getValue().getBoolean(MessageUtils.RESULT_FIELD));
        return argument.getValue();
    }

    @Test
    public void testDoAuthorizeKO() {
        final MockAutjManager mock = new MockAutjManager(true);
        final JsonObject returnValue = login(mock);
        final String session = returnValue.getString(AbstractAuthManager.SESSION_ID);
        assertNotNull(session);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject());
        mock.doAuthorise(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.DENIED_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoAuthorizeFalse() {
        final MockAutjManager mock = new MockAutjManager(true);
        final JsonObject returnValue = login(mock);
        final String session = returnValue.getString(AbstractAuthManager.SESSION_ID);
        assertNotNull(session);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.SESSION_ID, session + "1"));
        mock.authoriseHandler.handle(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.DENIED_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoAuthorizeOK() {
        final MockAutjManager mock = new MockAutjManager(true);
        final JsonObject returnValue = login(mock);
        final String session = returnValue.getString(AbstractAuthManager.SESSION_ID);
        assertNotNull(session);
        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.SESSION_ID, session));
        mock.doAuthorise(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertTrue(argument.getValue().getBoolean(MessageUtils.RESULT_FIELD));
    }

    @Test
    public void testDoLogoutKo() {
        final MockAutjManager mock = new MockAutjManager(true);


        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject());
        mock.logoutHandler.handle(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.ERROR_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoLogoutFalseSession() {
        final MockAutjManager mock = new MockAutjManager(true);


        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.SESSION_ID, "1"));

        mock.doLogout(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertEquals(MessageUtils.ERROR_STATUS, argument.getValue().getString(MessageUtils.STATUS_FIELD));
    }

    @Test
    public void testDoLogoutOk() {
        final MockAutjManager mock = new MockAutjManager(true);
        final JsonObject returnValue = login(mock);
        final String session = returnValue.getString(AbstractAuthManager.SESSION_ID);
        assertNotNull(session);

        final Message message = mock(Message.class);
        when(message.body()).thenReturn(new JsonObject().putString(AbstractAuthManager.SESSION_ID, session));

        mock.doLogout(message);
        final ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);
        verify(message, atLeastOnce()).reply(argument.capture());
        assertTrue(argument.getValue().getBoolean(MessageUtils.RESULT_FIELD));
    }

    @Test
    public void testStart() {
        final MockAutjManager mock = new MockAutjManager(true);
        mock.start();
        assertEquals(AbstractAuthManager.DEFAULT_SESSION_TIMEOUT, mock.getSessionTimeout());

    }

    @Test
    public void testStartSession() {
        final MockAutjManager mock = new MockAutjManager(true);
        mock.getContainer().config().putNumber(AbstractAuthManager.SESSION_TIMEOUT_FIELD, 1200L);
        mock.start();
        assertEquals(1200L, mock.getSessionTimeout());

    }

    public class MockAutjManager extends AbstractAuthManager {

        Vertx vertx;
        Container container;
        private boolean state = true;

        public MockAutjManager(boolean state) {
            super();
            this.state = state;
            vertx = mock(Vertx.class);
            when(vertx.eventBus()).thenReturn(mock(EventBus.class));
            container = mock(Container.class);
            when(container.config()).thenReturn(new JsonObject());
        }

        @Override
        protected boolean authenticate(String username, String password, final JsonObject config) {
            return state;
        }

        @Override
        public Vertx getVertx() {
            return vertx;
        }

        @Override
        public Container getContainer() {
            return container;
        }
    }


}
