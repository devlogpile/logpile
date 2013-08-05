package org.skarb.vertx.mods.test.integration;

import org.junit.Ignore;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import static org.vertx.testtools.VertxAssert.*;

/**
 * Default class for launching the mods.
 * User: skarb
 * Date: 30/07/13
 */
@Ignore
public class CouchBasePersistorLauncher extends TestVerticle {

    protected  JsonObject readConfig() {
        try {
            final URL resource = CouchBasePersistorLauncher.class.getClassLoader().getResource("config.json");
            final File file = new File(resource.toURI());
            final byte[] bytes = Files.readAllBytes(file.toPath());
            return new JsonObject(new String(bytes));
        } catch (Exception ex) {
            throw new IllegalStateException("error in reading", ex);
        }
    }

    protected JsonObject assertError(final Message<JsonObject> message) {
        final JsonObject body = message.body();
        assertEquals("Message : " + body.getString("message"), "ok", body.getString("status"));
        return body;
    }



    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        final JsonObject config = readConfig();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        final String moduleName = System.getProperty("vertx.modulename");
        container.deployModule(moduleName, config, new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });
    }

}
