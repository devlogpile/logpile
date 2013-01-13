package org.skarb.logpile.vertx.web;

import org.skarb.logpile.vertx.EventManager;
import org.skarb.logpile.vertx.MainVerticle;
import org.skarb.logpile.vertx.handler.DeployVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

/**
 * User: skarb
 * Date: 04/01/13
 */
public class LogpileWeb extends Verticle implements Handler<Message<JsonObject>> {

    static final String WEB_SERVER = "web_server";
    static final String SERVER_STATUS = "logpile.server-status";
    public static final String SERVICES_FIELD = "services";
    public static final String CONFIG_FIELD = "config";


    /**
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        // launch web server.
        final JsonObject configWebServer = getContainer().getConfig().getObject(WEB_SERVER);
        getContainer().deployModule("vertx.web-server-v1.0", configWebServer, configWebServer.getInteger(MainVerticle.INSTANCE_FIELD));
        // Authorization manager.
        getContainer().deployVerticle(AuthManagerJSonFile.class.getName(), getContainer().getConfig(), configWebServer.getInteger(MainVerticle.INSTANCE_FIELD),
                new DeployVerticle(getContainer(), AuthManagerJSonFile.class));

        //
        getVertx().eventBus().registerHandler(SERVER_STATUS,this);
    }


    @Override
    public void handle(final Message<JsonObject> message) {
       /* message.reply (
                new JsonObject().putObject(CONFIG_FIELD, getContainer().getConfig()));
       */
        getVertx().eventBus().send(EventManager.SERVICE_STATE, new JsonArray(), new Handler<Message<JsonArray>>() {
            @Override
            public void handle(Message<JsonArray> lgState) {
               message.reply (
                        new JsonObject().putArray(SERVICES_FIELD, lgState.body).putObject(CONFIG_FIELD, getContainer().getConfig()));
            }
        });
    }


}
