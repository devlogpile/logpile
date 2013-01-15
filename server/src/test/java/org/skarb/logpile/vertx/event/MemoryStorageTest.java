package org.skarb.logpile.vertx.event;


import org.junit.Test;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * User: skarb
 * Date: 06/01/13
 */
public class MemoryStorageTest {

    @Test
    public void testHandle() {
        final MemoryStorage memoryStorage = new MemoryStorage();
        assertEquals(0, memoryStorage.totalError);
        final Event event = Event.Builder(new JsonObject().putString("application", "app").putString("component", "1"));
        memoryStorage.handle(event);
        assertEquals(1, memoryStorage.totalError);
        assertEquals(1, memoryStorage.lastApplications.size());
        assertEquals(event.getApplication(), memoryStorage.lastApplications.get(0).applicationName);
        assertEquals(1, memoryStorage.lastApplications.get(0).countError);
        assertEquals(event.getComponent(), memoryStorage.lastApplications.get(0).lastComponents.get(0));


        final Event event2 = Event.Builder(new JsonObject().putString("application", "app").putString("component", "12"));
        memoryStorage.handle(event2);
        assertEquals(2, memoryStorage.totalError);
        assertEquals(1, memoryStorage.lastApplications.size());

        final Event event3 = Event.Builder(new JsonObject().putString("application", "app2").putString("component", "12"));
        memoryStorage.handle(event3);
        assertEquals(3, memoryStorage.totalError);
        assertEquals(2, memoryStorage.lastApplications.size());
    }

    @Test
    public void testdescribe() {
        final MemoryStorage memoryStorage = new MemoryStorage();
        assertNotNull(memoryStorage.describe());
    }

    @Test
    public void testsetVertx() {
        final MemoryStorage memoryStorage = new MemoryStorage();
        memoryStorage.setDatas(mock(Vertx.class), mock(Container.class));
    }

    @Test
    public void testHandleLimit() {
        final MemoryStorage memoryStorage = new MemoryStorage();

        for (int i = 0; i < 25; i++) {
            final Event event = Event.Builder(new JsonObject().putString("application", "app").putString("component", "c" + i));
            memoryStorage.handle(event);
        }
        assertEquals(25, memoryStorage.totalError);
        assertEquals(1, memoryStorage.lastApplications.size());
        assertEquals(10, memoryStorage.lastApplications.get(0).lastComponents.size());
    }

    @Test
    public void testErrorApplication() {
        final MemoryStorage.ErrorApplication ea = new MemoryStorage.ErrorApplication();
        ea.applicationName = "essai";
        ea.add("test");
        ea.add("test");
        final JsonObject jsonObject = ea.toJson();
        assertEquals("essai", jsonObject.getString(MemoryStorage.APP_NAME_FIELD));
        assertEquals(2, jsonObject.getNumber(MemoryStorage.APP_ERRORS_FIELD));
        final JsonArray array = jsonObject.getArray(MemoryStorage.COMPONENTS_LIST_FIELD);
        assertEquals(1, array.size());
        assertEquals("test", array.get(0).toString());

    }
}
