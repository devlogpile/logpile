package org.skarb.vertx.mods.command;

import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.impl.DefaultFutureResult;
import org.vertx.java.core.json.JsonObject;

/**
 * Default class for creating command.
 * User: skarb
 * Date: 30/07/13
 */
public abstract class Command implements Handler<AsyncResultHandler<JsonObject>> {

    protected CouchBasePersistor persistor;
    protected Message<JsonObject> message;

    public Command(CouchBasePersistor persistor, Message<JsonObject> message) {
        this.persistor = persistor;
        this.message = message;
    }

    public abstract JsonObject run() throws Exception;


    protected JsonObject getMandatoryObject(String field) {
        JsonObject val = message.body().getObject(field);
        if (val == null) {
            throw new IllegalArgumentException(field + " must be specified");
        }
        return val;
    }


    protected String getMandatoryString(String field) {
        String val = message.body().getString(field);
        if (val == null) {
            throw new IllegalArgumentException(field + " must be specified");
        }
        return val;
    }

    @Override
    public void handle(final AsyncResultHandler<JsonObject> handler) {
        final DefaultFutureResult<JsonObject> future = new DefaultFutureResult<>();
        future.setHandler(handler);

        try {
            if (persistor.client == null) {
                throw new IllegalStateException("client not connected");
            }

            final JsonObject reply = run();
            future.setResult(reply);
        } catch (final Throwable throwable) {
            future.setFailure(throwable);
        }
    }
}
