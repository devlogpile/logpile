package org.skarb.logpile.vertx.event;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Container;


/**
 * User: skarb
 * Date: 03/01/13
 */
abstract class AbstractEventMessage implements Handler<Message<JsonObject>> {


    public static final String RESULT = "result";
    private boolean active = true;
    private Vertx vertx;
    private Container container;

    public abstract boolean handle(final Event event);

    public abstract String describe();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDatas(final Vertx vertx, final Container container) {
        this.vertx = vertx;
        this.container = container;

    }

    public void handle(final Message<JsonObject> message) {
        final JsonObject replyMessage = new JsonObject();
        final boolean result = handle(Event.Builder(message.body));
        replyMessage.putBoolean(RESULT, result);
        message.reply(replyMessage);
    }

    public Vertx getVertx() {
        return vertx;
    }

    @SuppressWarnings("unused")
    public Container getContainer() {
        return container;
    }
}
