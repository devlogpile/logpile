package org.skarb.vertx.mods.command;

import net.spy.memcached.internal.OperationFuture;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

import java.util.UUID;

/**
 * Common pattern for storing commands.
 * User: skarb
 * Date: 30/07/13
 */
abstract class StoreCommand extends Command {


    public StoreCommand(final CouchBasePersistor persistor, final Message<JsonObject> message) {

        super(persistor, message);
    }

    public abstract OperationFuture<Boolean> run(final String key, final int timeout, final String doc) throws Exception;

    @Override
    public JsonObject run() throws Exception {
        final JsonObject document = getMandatoryObject("document");


        String key = message.body().getString("key");
        if (key == null) {
            final String incr = UUID.randomUUID().toString();
            key = persistor.bucket + ":" + incr;
        }

        final Number timeout = message.body().getNumber("timeout", persistor.timeout);

        final OperationFuture<Boolean> op = run(key, timeout.intValue(), document.encode());
        final JsonObject reply = new JsonObject();
        if (op.get()) {
            reply.putNumber("count", 1);
            reply.putString("key", key);
        } else {
            reply.putNumber("count", 0);
            reply.putString("message", op.getStatus().getMessage());
        }
        return reply;
    }
}
