package org.skarb.logpile.vertx.event;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Container;


/**
 * Abstract for the different handler to manage the event treatment.
 * User: skarb
 * Date: 03/01/13
 */
public abstract class AbstractEventMessage implements Handler<Message<JsonObject>> {

    /**
     * The result field of the response.
     */
    public static final String RESULT_FIELD = "result";
    /**
     * test if the handler is active to treament or not.
     */
    private boolean active = true;
    /**
     * the current instance.
     */
    private Vertx vertx;
    /**
     * the current instance.
     */
    private Container container;

    /**
     * Method to treat the reception of a new event.
     *
     * @param event the event.
     * @return if the treatment is ok or not.
     */
    public abstract boolean handle(final Event event);

    /**
     * The description used by the web console.
     *
     * @return the value to  display.
     */
    public abstract String describe();

    /**
     * GEtter on active property
     *
     * @return the value
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter on active property.
     *
     * @param active the value.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * init the vertx datas.
     *
     * @param vertx the current instance.
     * @param container the current instance.
     */
    public void setDatas(final Vertx vertx, final Container container) {
        this.vertx = vertx;
        this.container = container;

    }

    /**
     * Handle the message from the bus.
     *
     * @param message the message sent by Event Manager.
     */
    @Override
    public void handle(final Message<JsonObject> message) {
        final JsonObject replyMessage = new JsonObject();
        final boolean result = handle(Event.Builder(message.body()));
        replyMessage.putBoolean(RESULT_FIELD, result);
        message.reply(replyMessage);
    }

    /**
     * Getter on vertx property.
     *
     * @return vertx property.
     */
    public Vertx getVertx() {
        return vertx;
    }

    /**
     * Getter on container property.
     *
     * @return container property.
     */
    @SuppressWarnings("unused")
    public Container getContainer() {
        return container;
    }

    protected JsonObject getJsonObject() {
        JsonObject objectConfig = container.config().getObject(this.getClass().getName());
        if (objectConfig == null) {
            objectConfig = new JsonObject();
        }
        return objectConfig;
    }
}
