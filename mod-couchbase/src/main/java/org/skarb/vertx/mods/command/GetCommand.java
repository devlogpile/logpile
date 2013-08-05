package org.skarb.vertx.mods.command;

import org.skarb.vertx.mods.CouchBasePersistor;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * retreive an specific document with the key.
 * User: skarb
 * Date: 30/07/13
 */
public class GetCommand extends Command {


    public GetCommand(CouchBasePersistor persistor, Message<JsonObject> message) {
        super(persistor, message);
    }

    @Override
    public JsonObject run() throws Exception {
        final String key = getMandatoryString("key");

        final String resultString = (String) persistor.client.get(key);

        final JsonObject reply = new JsonObject();
        if(resultString == null) {
           reply.putNumber("count",0);
        } else {
            reply.putNumber("count",1);
            reply.putObject("document",new JsonObject(resultString));
        }
        return reply;
    }
}
