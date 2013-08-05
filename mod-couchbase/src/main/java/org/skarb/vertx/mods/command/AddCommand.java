package org.skarb.vertx.mods.command;

import net.spy.memcached.internal.OperationFuture;
import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * Call the add command
 * User: skarb
 * Date: 30/07/13
 */
public class AddCommand extends StoreCommand {

    public AddCommand(CouchBasePersistor persistor, Message<JsonObject> message) {
        super(persistor, message);
    }

    @Override
    public OperationFuture<Boolean> run(String key, int timeout, String doc) throws Exception {
        return persistor.client.add(key, timeout, doc);
    }
}
