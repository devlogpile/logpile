package org.skarb.logpile.vertx.event;


import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.impl.CaseInsensitiveMultiMap;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * User: skarb
 * Date: 03/01/13
 */
@SuppressWarnings("unchecked ")
public class EventManagerImplTest {

    @Test
    public void testhandle() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        eventManager.setServiceList(Collections.singletonList((AbstractEventMessage) new MockEM()));
        final Message message = mock(Message.class);
        eventManager.handle(message);
        final ArgumentCaptor<JsonArray> argument = ArgumentCaptor.forClass(JsonArray.class);
        verify(message).reply(argument.capture());
        assertEquals(1, argument.getValue().size());
        JsonObject o = (JsonObject) argument.getValue().get(0);
        assertEquals(MockEM.class.getName(), o.getString("name"));
        assertEquals(new MockEM().describe(), o.getString("describe"));
    }

    @Test
    public void testhandleEmpty() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Message message = mock(Message.class);
        eventManager.handle(message);
        final ArgumentCaptor<JsonArray> argument = ArgumentCaptor.forClass(JsonArray.class);
        verify(message).reply(argument.capture());
        assertEquals(0, argument.getValue().size());
    }

    @Test
    public void testinitEmpty() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);
        JsonObject config = new JsonObject();
        Container container = mock(Container.class);
        when(container.config()).thenReturn(config);
        eventManager.init(vertx, container);
        assertTrue(eventManager.getServiceList().isEmpty());

    }

    @Test
    public void testinit() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);

        EventBus eventBus = mock(EventBus.class);
        when(vertx.eventBus()).thenReturn(eventBus);

        JsonObject config = new JsonObject();
        JsonArray array = new JsonArray();
        array.addString(TmpTrue.class.getName());
        config.putArray(EventManagerImpl.FIELD_SERVICES, array);
        Container container = mock(Container.class);
        when(container.config()).thenReturn(config);
        eventManager.init(vertx, container);
        assertEquals(1, eventManager.getServiceList().size());
        assertTrue(eventManager.getServiceList().get(0) instanceof TmpTrue);

        verify(eventBus).registerHandler(eq(TmpTrue.class.getName()), (TmpTrue) anyObject());


    }

    @Test
    public void testinitException() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);

        EventBus eventBus = mock(EventBus.class);
        when(vertx.eventBus()).thenReturn(eventBus);


        JsonObject config = new JsonObject();
        JsonArray array = new JsonArray();
        array.addString("org.sss.Tmp");
        config.putArray(EventManagerImpl.FIELD_SERVICES, array);
        Container container = mock(Container.class);
        when(container.config()).thenReturn(config);
        eventManager.init(vertx, container);
        assertTrue(eventManager.getServiceList().isEmpty());
    }

    @Test
    public void testrunEmpty() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);

        final EventBus eventBus = mock(EventBus.class);
        when(vertx.eventBus()).thenReturn(eventBus);
        final Container container = mock(Container.class);
        when(container.config()).thenReturn(new JsonObject());
        eventManager.init(vertx, container);

        eventManager.run(new CaseInsensitiveMultiMap());

    }

    @Test
    public void testrunNotACtive() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);

        final EventBus eventBus = mock(EventBus.class);
        when(vertx.eventBus()).thenReturn(eventBus);
        final Container container = mock(Container.class);
        when(container.config()).thenReturn(new JsonObject());
        eventManager.init(vertx, container);

        TmpTrue tmp = new TmpTrue();
        tmp.setActive(false);
        eventManager.getServiceList().add(tmp);

        CaseInsensitiveMultiMap params = new CaseInsensitiveMultiMap();
        params.add(Event.PROP_APPLICATION, "appli1");
        eventManager.run(params);
        verify(eventBus, never()).send(anyString(), (JsonObject) any(), (Handler<Message<JsonObject>>) any());
    }

    @Test
    public void testrun() throws Exception {
        final EventManagerImpl eventManager = new EventManagerImpl();
        final Vertx vertx = mock(Vertx.class);

        final EventBus eventBus = mock(EventBus.class);
        when(vertx.eventBus()).thenReturn(eventBus);
        final Container container = mock(Container.class);
        when(container.config()).thenReturn(new JsonObject());

        eventManager.init(vertx, container);

        TmpTrue tmp = new TmpTrue();

        eventManager.getServiceList().add(tmp);

        CaseInsensitiveMultiMap params = new CaseInsensitiveMultiMap();
        params.add(Event.PROP_APPLICATION, "appli1");
        eventManager.run(params);
        ArgumentCaptor<JsonObject> argument = ArgumentCaptor.forClass(JsonObject.class);

        verify(eventBus, only()).send(eq(tmp.getClass().getName()), argument.capture(), (Handler<Message<JsonObject>>) any());
        assertNotNull(argument.getValue());
        assertEquals("appli1", argument.getValue().getString(Event.PROP_APPLICATION));
    }

    private static class MockEM extends AbstractEventMessage {
        @Override
        public boolean handle(Event event) {
            throw new RuntimeException("not yet implemengt");
        }

        @Override
        public String describe() {
            return "Une description";
        }
    }

    public static class TmpTrue extends AbstractEventMessage {
        @Override
        public boolean handle(final Event event) {
            //  System.out.println(event.getTime() + "/" + event.getApplication() + "/" + event.getComponent() + "/" + event.getMessage());
            return true;
        }

        @Override
        public String describe() {
            return "Console output";
        }
    }
}
