package org.skarb.logpile.vertx.event;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;


/**
 * User: skarb
 * Date: 03/01/13
 */
abstract class AbstractEventMessage implements Handler< Message <JsonObject> > {


    public static final String RESULT = "result";

    private boolean active = true;

    @SuppressWarnings("unused")
    private Vertx vertx;


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract boolean handle(final Event event);

    public abstract String describe();

    public void setVertx(final Vertx vertx){
        this.vertx = vertx;
    }

    public void handle(final Message<JsonObject> message) {
        final JsonObject replyMessage = new JsonObject();
        final boolean result  = handle(Event.Builder(message.body));
        replyMessage.putBoolean(RESULT, result);
        message.reply(replyMessage);
    }

    public Vertx getVertx() {
        return vertx;
    }
}
