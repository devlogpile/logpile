package org.skarb.vertx.mods;

import com.couchbase.client.CouchbaseClient;
import org.skarb.vertx.mods.command.*;
import org.vertx.java.busmods.BusModBase;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Persistor for couchbase DB.
 * User: skarb
 * Date: 30/07/13
 */
public class CouchBasePersistor extends BusModBase implements Handler<Message<JsonObject>> {

    public static final String DEFAULT_ADDRESS = "vertx.couchbasepersistor";
    public static final String DEFAULT_HOST = "http://127.0.0.1:8091/pools";
    public CouchbaseClient client;
    public int queryLimit;
    public int timeout;
    public String bucket;
    protected String address;
    protected String host;
    protected String password;

    @Override
    public void start() {
        super.start();

        address = getOptionalStringConfig("address", DEFAULT_ADDRESS);
        host = getOptionalStringConfig("host", DEFAULT_HOST);
        queryLimit = getOptionalIntConfig("query-limit", 200);
        timeout = getOptionalIntConfig("store-timeout", 0);
        password = getOptionalStringConfig("password", null);
        bucket = getOptionalStringConfig("bucket", "default");
        try {
            List<URI> hosts = Arrays.asList(
                    new URI(host)
            );

            client = new CouchbaseClient(hosts, bucket, password);

        } catch (Exception e) {
            logger.error("Failed to connect to couchbase server", e);
        }
        eb.registerHandler(address, this);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void handle(final Message<JsonObject> message) {

        final String action = message.body().getString("action");

        if (action == null) {
            sendError(message, "action must be specified");
            return;
        }


        final AsyncResultHandler<JsonObject> handler = new AsyncResultHandler<JsonObject>() {
            public void handle(AsyncResult<JsonObject> assyncResult) {
                if (assyncResult.succeeded()) {

                    sendOK(message, assyncResult.result());

                } else {
                    logger.error("error in calling service", assyncResult.cause());
                    sendError(message, assyncResult.cause().getMessage());
                }
            }
        };
        switch (action) {
            case "add":
                new AddCommand(this, message).handle(handler);
                break;
            case "set":
                new SetCommand(this, message).handle(handler);
                break;
            case "replace":
                new ReplaceCommand(this, message).handle(handler);
                break;
            case "get":
                new GetCommand(this, message).handle(handler);
                break;
            case "view":
                new ViewCommand(this, message).handle(handler);
                break;

            default:
                sendError(message, "Invalid action: " + action);
        }


    }

}
