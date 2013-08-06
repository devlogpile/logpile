package com.skarb.integration.java;


import org.junit.Test;
import org.skarb.vertx.mods.JmxManagement;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Example Java integration test that deploys the module that this project builds.
 * <p/>
 * Quite often in integration tests you want to deploy the same module for all tests and you don't want tests
 * to start before the module has been deployed.
 * <p/>
 * This test demonstrates how to do that.
 */

public class DoUpdateTest extends TestVerticle {


    @Test
    public void testField() {
        final JsonObject field = new JsonObject().putString("name", "myField").putString("value", "two");
        final JsonObject field2 = new JsonObject().putString("name", "myField2").putString("value", "one");
        final JsonObject datas = new JsonObject().putString(JmxManagement.FIELD_OBJECT_NAME
                , "com.skarb.vertx.mods:type=testField").putArray("fields", new JsonArray().addObject(field).addObject(field2));
        vertx.eventBus().send(JmxManagement.DEFAULT_ADDRESS + ".update", datas, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                assertEquals("ok", message.body().getString("status"));
                final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
                try {
                    final Object myField = platformMBeanServer.getAttribute(new ObjectName("com.skarb.vertx.mods:type=testField"), "myField");
                    assertEquals("two", myField);
                } catch (Exception ex) {
                    throw new IllegalStateException("error", ex);
                }
                testComplete();
            }
        });

    }

    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        final String property = System.getProperty("vertx.modulename");
        System.err.println(property);
        container.deployModule(property, new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                final JsonObject field = new JsonObject().putString("name", "myField").putString("value", "one").putString("description", " one test");
                final JsonObject datas = new JsonObject().putString("type", "testField").putArray("fields", new JsonArray().addObject(field));
                vertx.eventBus().send(JmxManagement.DEFAULT_ADDRESS + ".register", datas, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        assertEquals("ok", message.body().getString("status"));
                        startTests();
                    }
                });


            }
        });
    }


}
