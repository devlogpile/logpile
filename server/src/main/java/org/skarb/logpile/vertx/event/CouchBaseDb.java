package org.skarb.logpile.vertx.event;

import org.skarb.logpile.vertx.handler.HandlerUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;

import java.util.Map;

/**
 * Store the events in a couchbase DB.
 * <p>Use the mod couchbase module.</p>
 * User: skarb
 * Date: 05/08/13
 */
public class CouchBaseDb extends AbstractEventMessage {

    private String address;
    private JsonObject dbConfig;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatas(Vertx vertx, Container container) {
        super.setDatas(vertx, container);
        dbConfig = getJsonObject();
        if (dbConfig == null) {
            throw new IllegalStateException("An configuration is required to use this module.");
        }
        final String modCouchBase = dbConfig.getString("mod-coucbase");
        if (modCouchBase == null) {
            throw new IllegalStateException("the \"mod-coucbase\" parameter is required to use this module.");
        }
        final Number number = dbConfig.getNumber("nb-instance", 1);
        address = dbConfig.getString("address");
        if (address == null) {
            throw new IllegalStateException("the \"address\" parameter is required to use this module.");
        }


        getContainer().deployModule(modCouchBase, dbConfig, number.intValue(),
                HandlerUtils.deployVerticle(container, this.getClass()));


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handle(final Event event) {
        final JsonObject data = new JsonObject().putString("action", "add").putObject("document", event.toJson());


        getVertx().eventBus().send(address, data, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                if ("ok".equals(message.body().getString("status"))) {
                    getContainer().logger().debug("Save events in couchbase ok : " + event);
                } else {
                    getContainer().logger().error("Error in saving events : " + event + " / message : " + message.body().getString("message"));
                }
            }
        });

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String describe() {
        final StringBuilder message = new StringBuilder();

        if (dbConfig != null) {
            message.append("<strong>Configuration of the module :</strong>").append(NEW_LINE);

            for (Map.Entry<String, Object> entry : dbConfig.toMap().entrySet()) {
                message.append(entry.getKey()).append(" : ").append(entry.getValue()).append(NEW_LINE);
            }
        }
        return message.toString();
    }
}
