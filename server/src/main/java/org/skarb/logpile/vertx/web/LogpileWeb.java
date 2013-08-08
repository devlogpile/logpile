package org.skarb.logpile.vertx.web;

import org.skarb.logpile.vertx.EventManager;
import org.skarb.logpile.vertx.MainVerticle;
import org.skarb.logpile.vertx.handler.HandlerUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.file.impl.PathAdjuster;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.impl.VertxInternal;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Init the Web Server.
 * User: skarb
 * Date: 04/01/13
 */
public class LogpileWeb extends Verticle implements Handler<Message<JsonObject>> {

    public static final String SERVICES_FIELD = "services";
    public static final String CONFIG_FIELD = "config";
    static final String WEB_SERVER = "web_server";
    static final String SERVER_STATUS = "logpile.server-status";

    /**
     * start the Veticle.
     *
     * @throws Exception
     */
    @Override
    public void start() {
        // launch web server.
        final JsonObject configWebServer = getContainer().config().getObject(WEB_SERVER);
      /*  try{
        final Path adjust = PathAdjuster.adjust((VertxInternal) vertx, Paths.get("webapp/eventBus.json"));
            System.err.println("json file :"+adjust);
        }catch(Throwable t){
            t.printStackTrace();
        }
        getVertx().fileSystem().writeFileSync("webapp/eventBus.json",new Buffer(new JsonObject().putString("address","localhost:8082/events").encode()));
       */

        getContainer().deployModule("io.vertx~mod-web-server~2.0.0-final", configWebServer, configWebServer.getInteger(MainVerticle.INSTANCE_FIELD));
        // Authorization manager.
        getContainer().deployVerticle(AuthManagerJSonFile.class.getName(), getContainer().config(), configWebServer.getInteger(MainVerticle.INSTANCE_FIELD),
                HandlerUtils.deployVerticle(getContainer(), AuthManagerJSonFile.class));

        //
        getVertx().eventBus().registerHandler(SERVER_STATUS, this);
    }

    /**
     * Method to retrieve server properties.
     *
     * @param message the return messsage
     */
    @Override
    public void handle(final Message<JsonObject> message) {
        getVertx().eventBus().send(EventManager.SERVICE_STATE, new JsonArray(), createResponse(message, getContainer().config()));
    }

    /**
     * create an handler for the EventManager datas.
     *
     * @param message       the reply message.
     * @param configLogPile the configuration of the web server.
     * @return the handler.
     */
    Handler<Message<JsonArray>> createResponse(final Message<JsonObject> message, final JsonObject configLogPile) {
        return new Handler<Message<JsonArray>>() {
            @Override
            public void handle(Message<JsonArray> lgState) {
                message.reply(
                        new JsonObject().putArray(SERVICES_FIELD, lgState.body()).putObject(CONFIG_FIELD, configLogPile));
            }
        };
    }


}
