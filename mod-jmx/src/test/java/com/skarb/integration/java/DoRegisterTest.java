package com.skarb.integration.java;

import org.skarb.vertx.mods.JmxManagement;
import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Files;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Example Java integration test that deploys the module that this project builds.
 * <p/>
 * Quite often in integration tests you want to deploy the same module for all tests and you don't want tests
 * to start before the module has been deployed.
 * <p/>
 * This test demonstrates how to do that.
 */

public class DoRegisterTest extends TestVerticle {

    protected JsonObject readConfig() {
        try {
            final URL resource = DoRegisterTest.class.getClassLoader().getResource("config.json");
            final File file = new File(resource.toURI());
            final byte[] bytes = Files.readAllBytes(file.toPath());
            return new JsonObject(new String(bytes));
        } catch (Exception ex) {
            throw new IllegalStateException("error in reading", ex);
        }
    }

    @Test
    public void testRegister() {
        final JsonObject datas = new JsonObject().putString("type", "testRegister");
        vertx.eventBus().send(JmxManagement.DEFAULT_ADDRESS + ".register", datas, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                assertEquals("ok", message.body().getString("status"));
                assertEquals("com.skarb.vertx.mods:type=testRegister", message.body().getString("object-name"));
                testComplete();
            }
        });


    }

    @Test
    public void testOperation() {
        final JsonObject field = new JsonObject().putString("name", "myOp")
                .putString("reply-address", "test.call.ok.nobody")
                .putString("description", " one operation");
        final JsonObject datas = new JsonObject().putString("type", "testOperation").putArray("operations", new JsonArray().addObject(field));
        vertx.eventBus().send(JmxManagement.DEFAULT_ADDRESS + ".register", datas, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                assertEquals("ok", message.body().getString("status"));
                final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
                try {
                    platformMBeanServer.invoke(new ObjectName(message.body().getString("object-name")),"myOp", null,null);
                } catch (Exception ex) {
                    throw new IllegalStateException("error", ex);
                }
                testComplete();
            }
        });


    }

    @Test
    public void testField() {
        final JsonObject field = new JsonObject().putString("name", "myField").putString("value", "one").putString("description", " one test");
        final JsonObject datas = new JsonObject().putString("type", "testField").putArray("fields", new JsonArray().addObject(field));
        vertx.eventBus().send(JmxManagement.DEFAULT_ADDRESS + ".register", datas, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                assertEquals("ok", message.body().getString("status"));
                assertEquals("com.skarb.vertx.mods:type=testField", message.body().getString("object-name"));
                final MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
                try {
                    final Object myField = platformMBeanServer.getAttribute(new ObjectName(message.body().getString("object-name")), "myField");
                    assertEquals("one", myField);
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

                vertx.eventBus().registerHandler("test.call.ok.nobody", new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(final Message<JsonObject> message) {
                        assertNotNull(message.body());
                        message.reply(new JsonObject());
                        testComplete();
                    }
                });
                startTests();
            }
        });
    }


}
